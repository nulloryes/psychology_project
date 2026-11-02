<template>
    <div class="register-container">
        <div class="register-box">
            <h2>管理员注册</h2>
            <el-input placeholder="请输入账户" v-model="account">
                <template slot="prepend">账户</template>
            </el-input>
            <el-input type="password" placeholder="请输入密码" v-model="password">
                <template slot="prepend">密码</template>
            </el-input>
            <el-input type="password" placeholder="请确认密码" v-model="confirmPassword">
                <template slot="prepend">确认密码</template>
            </el-input>
            <el-input placeholder="请输入姓名" v-model="name">
                <template slot="prepend">姓名</template>
            </el-input>
            <div class="btn-group">
                <el-button type="primary" @click="handleRegister">注册</el-button>
                <el-button @click="goToLogin">返回登录</el-button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            account: null,
            password: null,
            confirmPassword: null,
            name: null,
        }
    },
    methods: {
        handleRegister() {
            if (!this.account || !this.password || !this.confirmPassword || !this.name) {
                this.$message({
                    message: "请填写完整信息",
                    type: "warning"
                })
                return
            }
            if (this.password !== this.confirmPassword) {
                this.$message({
                    message: "两次密码输入不一致",
                    type: "warning"
                })
                return
            }
            this.$axios.get("/admin/insertAdmin", {
                params: {
                    account: this.account,
                    password: this.password,
                    name: this.name,
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message({
                        message: "注册成功",
                        type: "success"
                    })
                    this.$router.push("/login")
                } else {
                    this.$message({
                        message: res.data.msg || "注册失败",
                        type: "error"
                    })
                }
            }).catch(error => {
                console.log(error)
                this.$message({
                    message: "注册失败",
                    type: "error"
                })
            })
        },
        goToLogin() {
            this.$router.push("/login")
        }
    },
}
</script>

<style scoped>
.register-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 20px;
}

.register-box {
    background: white;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    width: 400px;
}

.register-box h2 {
    text-align: center;
    margin-bottom: 30px;
    color: #333;
}

.register-box .el-input {
    margin-bottom: 20px;
}

.role-select {
    width: 100%;
    margin-bottom: 20px;
}

.btn-group {
    display: flex;
    justify-content: space-between;
}

.btn-group .el-button {
    width: 48%;
}
</style>
