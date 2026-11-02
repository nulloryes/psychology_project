<template>
    <div class="consultant-management-container">
        <el-card>
            <div slot="header">
                <span>咨询师管理</span>
            </div>

            <div class="search-bar">
                <el-input placeholder="搜索咨询师姓名" v-model="searchQuery" class="search-input" clearable>
                    <el-button slot="append" icon="el-icon-search" @click="searchConsultants"></el-button>
                </el-input>
                <el-select v-model="role" placeholder="选择职称" clearable @change="searchConsultants">
                    <el-option label="主任医师" value="主任医师"></el-option>
                    <el-option label="副主任医师" value="副主任医师"></el-option>
                    <el-option label="主治医师" value="主治医师"></el-option>
                    <el-option label="医师" value="医师"></el-option>
                </el-select>
                <el-select v-model="domainId" placeholder="选择领域" clearable @change="searchConsultants">
                    <el-option v-for="domain in domainList" :key="domain.domainId" :label="domain.name"
                        :value="domain.domainId"></el-option>
                </el-select>
                <el-select v-model="status" placeholder="是否审核通过" clearable @change="searchConsultants">
                    <el-option label="已通过" value=0></el-option>
                    <el-option label="未审核" value=1></el-option>
                </el-select>
                <el-button type="primary" size="small" @click="resetSearch">重置</el-button>
            </div>

            <el-table :data="consultantList" style="width: 100%" v-loading="loading">
                <el-table-column prop="consultantId" label="咨询师ID" width="80"></el-table-column>
                <el-table-column prop="name" label="姓名"></el-table-column>
                <el-table-column prop="role" label="职位"></el-table-column>
                <el-table-column prop="project" label="专业领域"></el-table-column>
                <el-table-column prop="school" label="毕业院校"></el-table-column>
                <el-table-column prop="price" label="咨询费用" width="100"></el-table-column>
                <el-table-column label="创建时间" width="200">
                    <template slot-scope="scope">
                        {{ scope.row.createTime | formatTime }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="300">
                    <template slot-scope="scope">
                        <el-button type="primary" size="small" @click="viewConsultant(scope.row)"
                            style="margin-right: 10px;">查看</el-button>
                        <span v-if="status == 1">
                            <el-button type="success" size="small"
                                @click="reviewConsultant(scope.row.consultantId)">通过</el-button>
                            <el-button type="danger" size="small"
                                @click="deleteConsultant(scope.row.consultantId)">不通过</el-button>
                        </span>
                        <span v-else>
                            <el-button type="success" size="small" @click="editConsultant(scope.row)">编辑</el-button>
                            <el-button type="danger" size="small"
                                @click="deleteConsultant(scope.row.consultantId)">删除</el-button>
                        </span>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
                layout="prev, pager, next, total" :total="total">
            </el-pagination>
        </el-card>

        <!-- 新增/编辑咨询师对话框 -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
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
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveConsultant">保存</el-button>
            </div>
        </el-dialog>

        <el-dialog title="查看咨询师" :visible.sync="infoVisible" width="600px">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="账户">{{ this.consultantForm.account }}</el-descriptions-item>
                <el-descriptions-item label="姓名">{{ this.consultantForm.name }}</el-descriptions-item>
                <el-descriptions-item label="毕业院校">{{ this.consultantForm.school }}</el-descriptions-item>
                <el-descriptions-item label="毕业专业">{{ this.consultantForm.project }}</el-descriptions-item>
                <el-descriptions-item label="咨询费用">{{ this.consultantForm.price }}</el-descriptions-item>
                <el-descriptions-item label="专业领域">
                    <p v-if="this.consultantForm.domain">
                        <span v-for="(item, index) in consultantForm.domain" :key="index">
                            {{ item.name }}
                            <span v-if="index < consultantForm.domain.length - 1">、</span>
                        </span>
                    </p>
                    <p v-else>{{ '暂无信息' }}</p>
                </el-descriptions-item>
                <el-descriptions-item label="个人简历">{{ this.consultantForm.profile }}</el-descriptions-item>
            </el-descriptions>
            <div slot="footer">
                <el-button @click="infoVisible = false">关闭</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
export default {
    data() {
        return {
            consultantList: [],
            domainList: [],
            loading: false,
            currentPage: 1,
            pageSize: 10,
            total: 0,
            searchQuery: '',
            domainId: null,
            role: null,
            status: null,
            dialogVisible: false,
            infoVisible: false,
            dialogTitle: '',
            consultantForm: {
                name: null,
                role: null,
                school: null,
                project: null,
                price: null,
                profile: null
            },
            domains: []
        }
    },
    methods: {
        getConsultants() {
            this.loading = true
            this.$axios.get("/consultant/allConsultant", {
                params: {
                    status: this.status,
                    name: this.searchQuery,
                    domainId: this.domainId,
                    role: this.role,
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            }).then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.consultantList = res.data.data.list || []
                    this.total = res.data.data.total || 0
                }
            }).catch(error => {
                this.loading = false
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
        searchConsultants() {
            this.currentPage = 1
            this.getConsultants()
        },
        resetSearch() {
            this.searchQuery = ''
            this.domainId = null
            this.role = null
            this.currentPage = 1
            this.getConsultants()
        },
        handleCurrentChange(val) {
            this.currentPage = val
            this.getConsultants()
        },
        viewConsultant(consultant) {
            this.$axios.get("/consultant/consultantInfo", {
                params: {
                    id: consultant.consultantId
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.consultantForm = res.data.data
                    this.infoVisible = true
                }
            }).catch(error => {
                console.log(error)
            })
        },
        editConsultant(consultant) {
            this.consultantForm = { ...consultant }
            this.domains = (consultant.domain || []).map(domain => domain.domainId);
            this.dialogTitle = '编辑咨询师'
            this.dialogVisible = true
        },
        reviewConsultant(id) {
            this.$confirm('确定审核通过该咨询师吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'success'
            }).then(() => {
                this.$axios.get("/admin/reviewConsultant", {
                    params: {
                        id: id
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message({
                            message: "审核成功",
                            type: "success"
                        })
                        this.getConsultants()
                    } else {
                        this.$message({
                            message: res.data.msg || "审核失败",
                            type: "error"
                        })
                    }
                })
                console.log(2222)
            }).catch(() => { })
        },
        saveConsultant() {
            if (!this.consultantForm.name && !this.consultantForm.role && !this.consultantForm.price) {
                this.$message({
                    message: "请填写必填字段",
                    type: "warning"
                })
                return
            }
            this.$axios.get("/consultant/updateConsultant", {
                params: {
                    consultantId: this.consultantForm.consultantId,
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
                        message: "保存成功",
                        type: "success"
                    })
                    this.dialogVisible = false
                    this.getConsultants()
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
        deleteConsultant(id) {
            this.$confirm('确定要删除该咨询师吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$axios.get("/consultant/delConsultant", {
                    params:{
                        id: id
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message({
                            message: "删除成功",
                            type: "success"
                        })
                        this.getConsultants()
                    } else {
                        this.$message({
                            message: res.data.msg || "删除失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    console.log(error)
                })
            }).catch(() => { })
        }
    },
    mounted() {
        this.getConsultants()
        this.getDomains()
    }
}
</script>

<style scoped>
.consultant-management-container {
    padding: 20px;
}

.search-bar {
    margin-bottom: 20px;
    display: flex;
    gap: 10px;
    align-items: center;
}

.search-input {
    width: 300px;
}
</style>
