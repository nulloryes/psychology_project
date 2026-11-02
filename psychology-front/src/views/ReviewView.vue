<template>
  <div class="review-container">
    <el-card class="review-card">
      <div slot="header">
        <span>评价咨询师</span>
      </div>
      
      <div class="appointment-info" v-if="appointment.reservationId">
        <h4>预约信息</h4>
        <p><strong>咨询师：</strong>{{ appointment.consultantName }}</p>
        <p><strong>预约时间：</strong>{{ appointment.orderTime }}</p>
      </div>
      
      <el-divider></el-divider>
      
      <div class="review-form">
        <div class="rating-section">
          <span class="label">评分：</span>
          <el-rate v-model="rating" show-score></el-rate>
        </div>
        
        <div class="content-section">
          <span class="label">评价内容：</span>
          <el-input
            type="textarea"
            :rows="6"
            placeholder="请分享您的咨询体验，帮助其他用户了解这位咨询师"
            v-model="content"
            maxlength="500"
            show-word-limit>
          </el-input>
        </div>
        
        <div class="form-actions">
          <el-button @click="goBack">返回</el-button>
          <el-button type="primary" @click="submitReview">提交评价</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
    data(){
        return{
            reservationId: null,
            appointment: {},
            rating: 5,
            content: null
        }
    },
    methods:{
        getAppointmentInfo(){
            this.$axios.get("/consultation/selectReservationById",{
                params:{
                    reservationId: this.reservationId
                }
            }).then(res=>{
                if(res.data.code == 1){
                    this.appointment = res.data.data || {}
                }
            }).catch(error =>{
                console.log(error)
            })
        },
        submitReview(){
            if(!this.content){
                this.$message({
                    message: "请输入评价内容",
                    type: "warning"
                })
                return
            }
            this.$axios.get("/consultation/updateReservation",{
                params:{
                    reservationId: this.reservationId,
                    rateScore: this.rating,
                    rate: this.content,
                }
            }).then(res=>{
                if(res.data.code == 1){
                    this.$message({
                        message: "评价成功",
                        type: "success"
                    })
                    this.$router.push("/appointment")
                }else{
                    this.$message({
                        message: res.data.msg || "评价失败",
                        type: "error"
                    })
                }
            }).catch(error =>{
                console.log(error)
            })
        },
        goBack(){
            this.$router.push("/appointment")
        }
    },
    mounted(){
        this.reservationId = this.$route.params.id
        this.getAppointmentInfo()
    }
}
</script>

<style scoped>
.review-container {
    padding: 20px;
    max-width: 800px;
    margin: 0 auto;
}
.appointment-info {
    margin-bottom: 20px;
}
.appointment-info h4 {
    margin-bottom: 10px;
}
.appointment-info p {
    margin: 5px 0;
    color: #666;
}
.review-form {
    margin-top: 20px;
}
.rating-section {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}
.label {
    margin-right: 10px;
    font-weight: bold;
}
.content-section {
    margin-bottom: 20px;
}
.form-actions {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 30px;
}
</style>
