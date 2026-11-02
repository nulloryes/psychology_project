<template>
    <div class="profile-container">
        <el-card>
            <div slot="header">
                <span>个人主页</span>
            </div>

            <div class="profile-content">
                <div class="profile-info">
                    <div class="avatar">
                        <el-avatar :size="100" :src="userInfo.headPicture || defaultAvatar">
                            {{ userInfo.name ? userInfo.name.charAt(0) : '管' }}
                        </el-avatar>
                    </div>
                    <div class="info-details">
                        <h3>{{ userInfo.name || '管理员' }}</h3>
                        <p><strong>角色：</strong>{{ getRoleName(userInfo.roleId) }}</p>
                        <p><strong>账户：</strong>{{ userInfo.account || '未知' }}</p>
                        <p><strong>钱包：</strong> {{ userInfo.money || 0 }} 元</p>
                        <p><strong>创建时间：</strong>{{ userInfo.createTime | formatTime }}</p>
                    </div>
                </div>

                <el-divider></el-divider>

                <div class="profile-actions">
                    <h4>账户管理</h4>
                    <el-button type="success" @click="moneyDialogVisible = true">钱包提现</el-button>
                    <el-button type="primary" @click="openEditDialog">编辑资料</el-button>
                    <el-button type="warning" @click="openChangePasswordDialog">修改密码</el-button>
                    <el-button type="success" @click="openInfoDialog" v-if="userInfo.roleId == 2">医师申请</el-button>
                </div>
            </div>
        </el-card>

        <!-- 编辑资料对话框 -->
        <el-dialog title="编辑资料" :visible.sync="editDialogVisible" width="500px">
            <el-form :model="editForm" label-width="100px">
                <el-form-item label="姓名">
                    <el-input v-model="editForm.name"></el-input>
                </el-form-item>
                <el-form-item label="头像">
                    <el-upload class="avatar-uploader" action="/admin/uploadAvatar" :show-file-list="false"
                        :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                        <img v-if="editForm.headPicture" :src="editForm.headPicture" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveProfile">保存</el-button>
            </div>
        </el-dialog>

        <!-- 修改密码对话框 -->
        <el-dialog title="修改密码" :visible.sync="passwordDialogVisible" width="400px">
            <el-form :model="passwordForm" label-width="100px">
                <el-form-item label="旧密码">
                    <el-input type="password" v-model="passwordForm.oldPassword"></el-input>
                </el-form-item>
                <el-form-item label="新密码">
                    <el-input type="password" v-model="passwordForm.newPassword"></el-input>
                </el-form-item>
                <el-form-item label="确认密码">
                    <el-input type="password" v-model="passwordForm.confirmPassword"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="passwordDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="changePassword">保存</el-button>
            </div>
        </el-dialog>

        <el-dialog title="医师申请" :visible.sync="infoVisible" width="500px">
            <el-form :model="consultantForm" label-width="100px">
                <el-form-item label="姓名">
                    <el-input v-model="consultantForm.name" placeholder="请输入姓名"></el-input>
                </el-form-item>
                <el-form-item label="职称">
                    <el-select v-model="consultantForm.role" placeholder="选择职称" clearable style="width: 100%">
                        <el-option label="主任医师" value="主任医师"></el-option>
                        <el-option label="副主任医师" value="副主任医师"></el-option>
                        <el-option label="主治医师" value="主治医师"></el-option>
                        <el-option label="医师" value="医师"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="专业领域">
                    <el-select v-model="domains" multiple style="width: 100%" placeholder="选择领域">
                        <el-option v-for="domain in domainList" :key="domain.domainId" :label="domain.name"
                            :value="domain.domainId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="毕业院校">
                    <el-input v-model="consultantForm.school" placeholder="请输入毕业院校"></el-input>
                </el-form-item>
                <el-form-item label="毕业专业">
                    <el-input v-model="consultantForm.project" placeholder="请输入毕业专业"></el-input>
                </el-form-item>
                <el-form-item label="咨询费用">
                    <el-input v-model="consultantForm.price" type="number" placeholder="请输入咨询费用"></el-input>
                </el-form-item>
                <el-form-item label="个人简介">
                    <el-input type="textarea" v-model="consultantForm.profile" :rows="3"
                        placeholder="编辑个人简介"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="infoVisible = false">取消</el-button>
                <el-button type="primary" @click="addConsultant">提交申请</el-button>
            </div>
        </el-dialog>

        <el-dialog title="钱包提现" :visible.sync="moneyDialogVisible" width="400px">
            <el-form>
                <el-form-item label="提现金额">
                    <el-input v-model="handleMoney.getMoney" clearable></el-input>
                </el-form-item>
                <el-form-item label="选择提现方式">
                    <el-radio-group v-model="handleMoney.getMethod" class="pay-radio-group">
                        <el-radio label="微信" class="pay-radio-item">
                            <div class="pay-method-content">
                                <i class="el-icon-chat-dot-round"></i>
                                <span>微信提现</span>
                            </div>
                        </el-radio>
                        <el-radio label="支付宝" class="pay-radio-item">
                            <div class="pay-method-content">
                                <i class="el-icon-bank-card"></i>
                                <span>支付宝提现</span>
                            </div>
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="moneyDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleMoneys" :disabled="!handleMoney.getMethod">确认提现</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            userInfo: {
                id: -1,
                name: '',
                account: '',
                phone: '',
                roleId: -1,
                createTime: ''
            },
            defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
            moneyDialogVisible: false,
            editDialogVisible: false,
            passwordDialogVisible: false,
            infoVisible: false,
            editForm: {
                headPicture: '',
                name: ''
            },
            consultantForm: {
                name: null,
                role: null,
                school: null,
                project: null,
                price: null,
                profile: null
            },
            passwordForm: {
                oldPassword: '',
                newPassword: '',
                confirmPassword: ''
            },
            handleMoney: {
                getMoney: null,
                getMethod: null
            },
            domainList: [],
            domains: []
        }
    },
    methods: {
        getProfile() {
            this.$axios.get("/admin/adminInfo", {
                params: {
                    id: this.$store.state.adminId
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.userInfo = res.data.data
                    this.userInfo.headPicture = null
                }
            }).catch(error => {
                console.log(error)
            })
        },
        getDomains() {
            this.$axios.get("/domain/allDomain").then(res => {
                if (res.data.code == 1) {
                    this.domainList = res.data.data || []
                }
            }).catch(error => {
                console.log(error)
            })
        },
        getRoleName(roleId) {
            const roles = {
                1: '超级管理员',
                2: '普通管理员',
                3: '咨询师'
            }
            return roles[roleId] || '未知角色'
        },
        openEditDialog() {
            this.editForm = { ...this.userInfo }
            this.editDialogVisible = true
        },
        openChangePasswordDialog() {
            this.passwordForm = {
                oldPassword: '',
                newPassword: '',
                confirmPassword: ''
            }
            this.passwordDialogVisible = true
        },
        openInfoDialog() {
            this.domains = []
            this.consultantForm.school = null,
                this.consultantForm.role = null,
                this.consultantForm.profile = null,
                this.consultantForm.project = null,
                this.consultantForm.price = null,
                this.consultantForm.name = this.userInfo.name
            this.infoVisible = true
        },
        handleMoneys() {
            if (!this.handleMoney.getMethod) {
                this.$message.warning('请选择支付方式')
                return
            }
            if (this.userInfo.money < this.handleMoney.getMoney) {
                this.$message.warning('提现金额超过钱包数额')
                return
            }
            const money = this.userInfo.money-this.handleMoney.getMoney
            this.$axios.get("/admin/updateAdmin",{
                params:{
                    adminId: this.$store.state.adminId,
                    money: money
                }
            }).then(res=>{
                if(res.data.code == 1){
                    this.$message({
                        message: "提现成功",
                        type: "success"
                    })
                    this.moneyDialogVisible = false
                    this.getProfile()
                }
            }).catch(error => {
                console.log(error)
            })
        },
        saveProfile() {
            if (!this.editForm.name) {
                this.$message({
                    message: "请填写姓名",
                    type: "warning"
                })
                return
            }
            this.$axios.get("/admin/updateAdmin", {
                params: {
                    adminId: this.$store.state.adminId,
                    headPicture: this.editForm.headPicture,
                    name: this.editForm.name
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message({
                        message: "保存成功",
                        type: "success"
                    })
                    this.editDialogVisible = false
                    this.getProfile()
                } else {
                    this.$message({
                        message: res.data.msg || "保存失败",
                        type: "error"
                    })
                }
            }).catch(error => {
                console.log(error)
            })
        },
        changePassword() {
            if (!this.passwordForm.oldPassword || !this.passwordForm.newPassword || !this.passwordForm.confirmPassword) {
                this.$message({
                    message: "请填写所有密码字段",
                    type: "warning"
                })
                return
            }
            if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
                this.$message({
                    message: "两次密码不一致",
                    type: "warning"
                })
                return
            }
            this.$axios.get("/admin/updateAdmin", {
                params: {
                    adminId: this.$store.state.adminId,
                    oldPassword: this.passwordForm.oldPassword,
                    password: this.passwordForm.newPassword
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message({
                        message: "密码修改成功",
                        type: "success"
                    })
                    this.passwordDialogVisible = false
                } else {
                    this.$message({
                        message: res.data.msg || "密码修改失败",
                        type: "error"
                    })
                }
            }).catch(error => {
                console.log(error)
            })
        },
        addConsultant() {
            if (!this.consultantForm.name && !this.consultantForm.role && !this.consultantForm.price) {
                this.$message({
                    message: "请填写必填字段",
                    type: "warning"
                })
                return
            }
            console.log(this.consultantForm)
            this.$axios.get("/admin/insertConsultant", {
                params: {
                    adminId: this.$store.state.adminId,
                    name: this.consultantForm.name,
                    school: this.consultantForm.school,
                    role: this.consultantForm.role,
                    profile: this.consultantForm.profile,
                    project: this.consultantForm.project,
                    price: this.consultantForm.price,
                    domains: this.domains
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message({
                        message: "申请成功，等待审核",
                        type: "success"
                    })
                    this.infoVisible = false
                } else {
                    this.$message({
                        message: res.data.msg || "申请失败",
                        type: "error"
                    })
                }
            }).catch(error => {
                console.log(error)
            })
        },
        handleAvatarSuccess(response) {
            if (response.code == 1) {
                this.editForm.avatar = response.data
                this.$message({
                    message: "上传成功",
                    type: "success"
                })
            }
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
            const isLt2M = file.size / 1024 / 1024 < 2
            if (!isJPG) {
                this.$message.error('只能上传JPG/PNG图片!')
            }
            if (!isLt2M) {
                this.$message.error('图片大小不能超过2MB!')
            }
            return isJPG && isLt2M
        }
    },
    mounted() {
        this.getProfile()
        this.getDomains()
    }
}
</script>

<style scoped>
.profile-container {
    padding: 20px;
}

.profile-content {
    padding: 20px 0;
}

.profile-info {
    display: flex;
    align-items: center;
    margin-bottom: 30px;
}

.avatar {
    margin-right: 30px;
}

.avatar img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    object-fit: cover;
}

.info-details h3 {
    margin: 0 0 15px 0;
    color: #333;
}

.info-details p {
    margin: 8px 0;
    color: #666;
}

.profile-actions {
    margin-top: 20px;
}

.profile-actions h4 {
    color: #333;
    margin-bottom: 15px;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
}

.avatar {
    width: 100px;
    height: 100px;
    display: block;
}

.pay-radio-group {
    display: flex;
    flex-direction: column;
    width: 100%;
}

.pay-method-content {
    display: flex;
    align-items: center;
    font-size: 16px;
}

.pay-method-content i {
    font-size: 24px;
    margin-right: 15px;
    color: #409EFF;
}

.pay-method-content .balance {
    color: #909399;
    font-size: 14px;
    margin-left: 10px;
}

.pay-radio-item {
    margin: 10px 0;
    padding: 15px;
    border: 1px solid #DCDFE6;
    border-radius: 8px;
    transition: all 0.3s;
}

.pay-radio-item:hover {
    border-color: #409EFF;
    background-color: #f5f7fa;
}
</style>
