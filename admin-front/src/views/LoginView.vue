<template>
    <div class="login-container">
        <div class="login-box">
            <h2>管理员登录</h2>
            <el-input placeholder="请输入账户" v-model="account">
                <template slot="prepend">账户</template>
            </el-input>
            <el-input type="password" placeholder="请输入密码" v-model="password">
                <template slot="prepend">密码</template>
            </el-input>
            <el-input placeholder="请输入验证码" v-model="code">
                <template slot="prepend">验证码</template>
            </el-input>
            <img @click="getCode" :src="`data:image/png;base64,${codeImage}`" alt="验证码" class="code-img">
            <div class="btn-group">
                <el-button type="primary" @click="getLogin">登录</el-button>
                <el-button @click="goToRegister">注册</el-button>
            </div>
        </div>
    </div>
</template>

<script>
import authToken from '@/utils/authToken';
export default {
    data() {
        return {
            account: null,
            password: null,
            code: null,
            codeImage: null,
            unique: null
        }
    },
    methods: {
        getLogin() {
            this.$store.commit('RESETADMIN')
            const loading = this.$loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            this.$axios.post("/admin/login", {
                account: this.account,
                password: this.password,
                code: this.code,
                unique: this.unique
            }).then(async res => {
                if (res.data.code == 1) {
                    this.$message({
                        message: "登录成功",
                        type: "success"
                    })
                    authToken.setToken(res.data.data)
                    const success = await this.$store.dispatch('getadmininfo')
                    this.account = null
                    this.password = null
                    if (success) {
                        loading.close();
                        this.$router.push("/main/dashboard")
                    } else {
                        this.$message({
                            message: "加载失败，请重试",
                            type: "warning"
                        })
                    }
                } else if (res.data.code == -2) {
                    this.$message({
                        message: "验证码错误",
                        type: "warning"
                    })
                } else if (res.data.code == -1) {
                    this.$message({
                        message: "账户或密码错误",
                        type: "warning"
                    })
                } else {
                    this.$message({
                        message: '其他错误，请联系管理员',
                        type: "error"
                    });
                }
                loading.close();
                setTimeout(() => {
                    this.getCode();
                }, 1500);
            }).catch(error => {
                console.log(error)
                loading.close();
                setTimeout(() => {
                    this.getCode();
                }, 1500);
            })
        },
        getCode() {
            this.$axios.get("/code/getCode", {
                params: {
                    uniqueRequest: this.unique
                }
            })
                .then(res => {
                    this.codeImage = res.data.data.CodeImage
                    this.unique = res.data.data.unique
                })
        },
        goToRegister() {
            this.$router.push("/register")
        }
    },
    mounted() {
        this.getCode();
    }
}
</script>

<style scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
    background: white;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    width: 400px;
}

.login-box h2 {
    text-align: center;
    margin-bottom: 30px;
    color: #333;
}

.login-box .el-input {
    margin-bottom: 20px;
}

.code-img {
    cursor: pointer;
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
