<template>
  <div class="register-container">
    <div class="register-box">
      <h2>用户注册</h2>
      <el-input placeholder="请输入账户" v-model="account" clearable>
        <template slot="prepend">账户</template>
      </el-input>
      <el-input type="password" placeholder="请输入密码" v-model="password" clearable>
        <template slot="prepend">密码</template>
      </el-input>
      <el-input type="password" placeholder="请确认密码" v-model="confirmPassword" clearable>
        <template slot="prepend">确认密码</template>
      </el-input>
      <el-input placeholder="请输入姓名" v-model="name" clearable>
        <template slot="prepend">姓名</template>
      </el-input>
      <el-input placeholder="请输入年龄" v-model="age" type="number" clearable>
        <template slot="prepend">年龄</template>
      </el-input>
      <el-select v-model="gender" placeholder="请选择性别" class="gender-select" clearable>
        <el-option label="男" value="男"></el-option>
        <el-option label="女" value="女"></el-option>
      </el-select>
      <el-input placeholder="请输入手机号" v-model="phone" clearable>
        <template slot="prepend">手机号</template>
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
    data(){
        return{
            account: null,
            password: null,
            confirmPassword: null,
            name: null,
            age: null,
            gender: null,
            phone: null
        }
    },
    methods:{
        handleRegister(){
            if(!this.account || !this.password || !this.confirmPassword || !this.name){
                this.$message({
                    message: "请填写完整信息",
                    type: "warning"
                })
                return
            }
            if(this.password !== this.confirmPassword){
                this.$message({
                    message: "两次密码输入不一致",
                    type: "warning"
                })
                return
            }
            const phoneReg = /^1[3-9]\d{9}$/;
            if(!phoneReg.test(this.phone)){
                    this.$message({
                    message: "电话格式错误",
                    type: "warning"
                })
                return
            }
            this.$axios.get("/user/enroll",{
                params:{
                    account: this.account,
                    password: this.password,
                    name: this.name,
                    age: this.age,
                    sex: this.gender,
                    phone: this.phone
                }
            }).then(res=>{
                if(res.data.code == 1){
                    this.$message({
                        message: "注册成功",
                        type: "success"
                    })
                    this.$router.push("/login")
                }else{
                    this.$message({
                        message: res.data.msg || "注册失败",
                        type: "error"
                    })
                }
            }).catch(error =>{
                console.log(error)
                this.$message({
                    message: "注册失败",
                    type: "error"
                })
            })
        },
        goToLogin(){
            this.$router.push("/login")
        }
    },
    mounted(){
    }
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
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
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
.gender-select {
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
