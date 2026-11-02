<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="profile-card">
          <div class="profile-header">
            <el-upload class="avatar-uploader" action="https://jsonplaceholder.typicode.com/posts/"
              :show-file-list="false" :on-success="handleAvatarSuccess"
              :headers="{ 'Content-Type': 'multipart/form-data' }">
              <img v-if="userInfo.avatar" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <h3>{{ userInfo.name }}</h3>
            <p class="account">账号：{{ userInfo.account }}</p>
          </div>
          <el-divider></el-divider>
          <div class="profile-stats">
            <div class="stat-item">
              <span class="stat-value">¥{{ userInfo.money || 0 }}</span>
              <span class="stat-label">账户余额</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ appointmentCount }}</span>
              <span class="stat-label">预约次数</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ reportCount }}</span>
              <span class="stat-label">评估次数</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="3">
        <el-card class="selectMenu">
          <span>快捷菜单</span>
        <el-menu :default-active="activeMenu" @select="handleMenuSelect">
          <el-menu-item index="info">
            <i class="el-icon-user"></i>
            <span slot="title">个人信息</span>
          </el-menu-item>
          <el-menu-item index="appointments">
            <i class="el-icon-date"></i>
            <span slot="title">我的预约</span>
          </el-menu-item>
          <el-menu-item index="reports">
            <i class="el-icon-document"></i>
            <span slot="title">我的报告</span>
          </el-menu-item>
          <el-menu-item index="records">
            <i class="el-icon-wallet"></i>
            <span slot="title">消费记录</span>
          </el-menu-item>
          <el-menu-item index="recharge">
            <i class="el-icon-money"></i>
            <span slot="title">账户充值</span>
          </el-menu-item>
        </el-menu>
        </el-card>
      </el-col>

      <el-col :span="13">
        <!-- 个人信息 -->
        <el-card v-if="activeMenu === 'info'" class="content-card">
          <div slot="header">
            <span>个人信息</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="editMode = !editMode">
              {{ editMode ? '取消' : '编辑' }}
            </el-button>
          </div>
          <el-form :model="userInfo" label-width="100px" :disabled="!editMode">
            <el-form-item label="姓名">
              <el-input v-model="userInfo.name"></el-input>
            </el-form-item>
            <el-form-item label="年龄">
              <el-input v-model="userInfo.age" type="number"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-select v-model="userInfo.sex" style="width: 100%">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="userInfo.phone"></el-input>
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="userInfo.address"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="userInfo.password"></el-input>
            </el-form-item>
            <el-form-item v-if="editMode">
              <el-button type="primary" @click="saveUserInfo">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 我的预约 -->
        <el-card v-if="activeMenu === 'appointments'" class="content-card">
          <div slot="header">
            <span>我的预约</span>
          </div>
          <el-table :data="appointmentList" style="width: 100%">
            <el-table-column prop="consultantName" label="咨询师"></el-table-column>
            <el-table-column prop="orderTime" label="预约日期"></el-table-column>
            <el-table-column prop="status" label="状态">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <!-- 我的报告 -->
        <el-card v-if="activeMenu === 'reports'" class="content-card">
          <div slot="header">
            <span>我的报告</span>
          </div>
          <el-tabs v-model="reportTab">
            <el-tab-pane label="咨询报告" name="consultation">
              <el-table :data="consultationReports" style="width: 100%">
                <el-table-column prop="consultantName" label="咨询师"></el-table-column>
                <el-table-column prop="orderTime" label="咨询日期"></el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button type="primary" size="mini" @click="viewReport(scope.row)">查看</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="评估报告" name="evaluation">
              <el-table :data="evaluationReports" style="width: 100%">
                <el-table-column prop="domainName" label="问题类型" width="150px"></el-table-column>
                <el-table-column prop="score" label="得分" width="100px"></el-table-column>
                <el-table-column prop="tip" label="评估结果"></el-table-column>
                <el-table-column label="完成时间">
                  <template slot-scope="scope">
                    {{ scope.row.createTime | formatTime }}
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-card>

        <!-- 消费记录 -->
        <el-card v-if="activeMenu === 'records'" class="content-card">
          <div slot="header">
            <span>消费账单</span>
          </div>
          <el-table :data="moneyFlowList" style="width: 100%">
            <el-table-column label="时间">
              <template slot-scope="scope">
                {{ scope.row.createTime | formatTime }}
              </template>
            </el-table-column>
            <el-table-column prop="tip" label="事项">
            </el-table-column>
            <el-table-column prop="money" label="金额">
            </el-table-column>
            <el-table-column prop="type" label="支付类型">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.type">{{ scope.row.type }}</el-tag>
                <el-button v-else type="warning" size="small" @click="payMoney(scope.row.moneyFlowId)">去支付</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注">
              <template slot-scope="scope">
                <div v-if="scope.row.name">
                  咨询师：{{ scope.row.name }}
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <!-- 账户充值 -->
        <el-card v-if="activeMenu === 'recharge'" class="content-card">
          <div slot="header">
            <span>账户充值</span>
          </div>
          <div class="recharge-section">
            <p class="current-balance">当前余额：<span>¥{{ userInfo.money || 0 }}</span></p>
            <div class="recharge-amount">
              <span class="label">充值金额：</span>
              <el-radio-group v-model="rechargeAmount">
                <el-radio-button label="50">50元</el-radio-button>
                <el-radio-button label="100">100元</el-radio-button>
                <el-radio-button label="200">200元</el-radio-button>
                <el-radio-button label="500">500元</el-radio-button>
              </el-radio-group>
            </div>
            <div class="custom-amount">
              <span class="label">自定义金额：</span>
              <el-input v-model="customRechargeAmount" placeholder="请输入充值金额" style="width: 200px">
                <template slot="append">元</template>
              </el-input>
            </div>
            <el-button type="primary" size="large" @click="visibleRecharge" class="recharge-btn">立即充值</el-button>
          </div>
        </el-card>
      </el-col>
      
    </el-row>

    <!-- 报告详情弹窗 -->
    <el-dialog title="报告详情" :visible.sync="reportDialogVisible" width="600px">
      <div v-if="currentReport.reservationId" class="report-detail">
        <h4>基本信息</h4>
        <p><strong>咨询师：</strong>{{ currentReport.consultantName }}</p>
        <p><strong>咨询日期：</strong>{{ currentReport.orderTime }}</p>
        <p><strong>咨询领域：</strong>{{ currentReport.domainName }}</p>
        <el-divider></el-divider>
        <h4>咨询报告</h4>
        <div class="report-content">{{ currentReport.problem || '暂无报告内容' }}</div>
        <el-divider></el-divider>
        <h4>专家建议</h4>
        <div class="suggestion-content">{{ currentReport.answer || '暂无建议' }}</div>
      </div>
    </el-dialog>

    <el-dialog title="选择支付方式" :visible.sync="payDialogVisible" width="400px" :close-on-click-modal="false">
      <div class="pay-dialog-content">
        <div class="pay-info" v-if="currentPayOrder">
          <p><strong>支付金额：</strong><span class="pay-amount">¥{{ currentPayOrder.money }}</span></p>
        </div>
        <el-divider></el-divider>
        <div class="pay-methods">
          <el-radio-group v-model="payMethod" class="pay-radio-group">
            <el-radio label="钱包" class="pay-radio-item">
              <div class="pay-method-content">
                <i class="el-icon-wallet"></i>
                <span>钱包支付</span>
                <span class="balance" v-if="userInfo.money !== undefined">（余额：¥{{ userInfo.money }}）</span>
              </div>
            </el-radio>
            <el-radio label="微信" class="pay-radio-item">
              <div class="pay-method-content">
                <i class="el-icon-chat-dot-round"></i>
                <span>微信支付</span>
              </div>
            </el-radio>
            <el-radio label="支付宝" class="pay-radio-item">
              <div class="pay-method-content">
                <i class="el-icon-bank-card"></i>
                <span>支付宝支付</span>
              </div>
            </el-radio>
          </el-radio-group>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPay" :loading="payLoading" :disabled="!payMethod">确认支付</el-button>
      </span>
    </el-dialog>

    <el-dialog title="选择支付方式" :visible.sync="payAccount" width="400px" :close-on-click-modal="false">
      <el-radio-group v-model="payMethod" class="pay-radio-group">
        <el-radio label="微信" class="pay-radio-item">
          <div class="pay-method-content">
            <i class="el-icon-chat-dot-round"></i>
            <span>微信支付</span>
          </div>
        </el-radio>
        <el-radio label="支付宝" class="pay-radio-item">
          <div class="pay-method-content">
            <i class="el-icon-bank-card"></i>
            <span>支付宝支付</span>
          </div>
        </el-radio>
      </el-radio-group>
      <span slot="footer" class="dialog-footer">
        <el-button @click="payAccount = false">取消</el-button>
        <el-button type="primary" @click="handleRecharge" :disabled="!payMethod">确认支付</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {},
      activeMenu: 'info',
      editMode: false,
      appointmentList: [],
      appointmentCount: 0,
      reportCount: 0,
      reportTab: 'consultation',
      consultationReports: [],
      evaluationReports: [],
      moneyFlowList: [],
      rechargeAmount: 100,
      customRechargeAmount: null,
      reportDialogVisible: false,
      currentReport: {},
      imageUrl: '',
      payDialogVisible: false,
      payAccount: false,
      payMethod: '',
      currentPayOrder: null,
      payLoading: false
    }
  },
  methods: {
    getUserInfo() {
      this.$axios.get("/user/getUserInfo").then(res => {
        if (res.data.code == 1) {
          const userId = res.data.data.userId
          this.getUserDetail(userId)
        }
      }).catch(error => {
        console.log(error)
      })
    },
    getUserDetail(userId) {
      this.$axios.get("/user/userInfo", {
        params: {
          id: userId
        }
      }).then(res => {
        if (res.data.code == 1) {
          this.userInfo = res.data.data || {}
          this.imageUrl = res.data.data.avatar || ''
          // 确保 sex 字段与 gender 字段同步，使表单正确显示
          if (this.userInfo.gender && !this.userInfo.sex) {
            this.userInfo.sex = this.userInfo.gender
          } else if (this.userInfo.sex && !this.userInfo.gender) {
            this.userInfo.gender = this.userInfo.sex
          }
        }
      }).catch(error => {
        console.log(error)
      })
    },
    saveUserInfo() {
      this.$axios.get("/user/updateUser", {
        params: {
          userId: this.userInfo.userId,
          name: this.userInfo.name,
          age: this.userInfo.age,
          gender: this.userInfo.sex,
          phone: this.userInfo.phone,
          address: this.userInfo.address,
          password: this.userInfo.password,
          avatar: this.userInfo.avatar
        }
      }).then(res => {
        if (res.data.code == 1) {
          this.$message({
            message: "保存成功",
            type: "success"
          })
          this.editMode = false
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
    getAppointmentList() {
      this.$axios.get("/consultation/selectReservation", {
        params: {
          userId: this.$store.state.userId
        }
      }).then(res => {
        if (res.data.code == 1) {
          this.appointmentList = res.data.data.list || []
          this.appointmentCount = this.appointmentList.length
        }
      }).catch(error => {
        console.log(error)
      })
    },
    getReports() {
      this.$axios.get("/consultation/selectReservation", {
        params: {
          userId: this.$store.state.userId,
          status: 2
        }
      }).then(res => {
        if (res.data.code == 1) {
          this.consultationReports = res.data.data.list || []
        }
      }).catch(error => {
        console.log(error)
      })

      this.$axios.get("/question/selectMyReport", {
        params: {
          userId: this.$store.state.userId
        }
      }).then(res => {
        if (res.data.code == 1) {
          this.evaluationReports = res.data.data.list || []
          this.reportCount = this.evaluationReports.length
        }
      }).catch(error => {
        console.log(error)
      })
    },
    getMoneyFlow() {
      this.$axios.get("/river/selectRiverByUserId", {
        params: {
          userId: this.$store.state.userId
        }
      }).then(res => {
        if (res.data.code == 1) {
          this.moneyFlowList = res.data.data || []
        }
      }).catch(error => {
        console.log(error)
      })
    },
    visibleRecharge() {
      this.payMethod = ''
      this.payAccount = true
    },
    handleRecharge() {
      const amount = this.customRechargeAmount || this.rechargeAmount
      if (!amount || amount <= 0) {
        this.$message({
          message: "请输入有效的充值金额",
          type: "warning"
        })
        return
      }
      this.$axios.get("/river/insertMoney", {
        params: {
          userId: this.$store.state.userId,
          money: amount,
          type: this.payMethod,
          status: 1,
          tip: '账户充值'
        }
      }).then(res => {
        if (res.data.code == 1) {
          this.$message({
            message: "充值成功",
            type: "success"
          })
          this.getUserInfo()
          this.getMoneyFlow()
          this.customRechargeAmount = null
          this.payAccount = false
        } else {
          this.$message({
            message: res.data.msg || "充值失败",
            type: "error"
          })
        }
      }).catch(error => {
        console.log(error)
      })
    },
    handleMenuSelect(index) {
      this.activeMenu = index
      this.editMode = false
      if (index === 'appointments') {
        this.getAppointmentList()
      } else if (index === 'reports') {
        this.getReports()
      } else if (index === 'records') {
        this.getMoneyFlow()
      }
    },
    viewReport(row) {
      this.currentReport = row
      this.reportDialogVisible = true
    },
    handleAvatarSuccess(res, file) {
      if (res.code === 1) {
        // 后端返回成功，使用后端返回的头像URL
        this.imageUrl = res.data.data.avatar;
        // 更新用户信息中的头像
        this.userInfo.avatar = this.imageUrl;
        // 保存用户信息，更新头像
        this.saveUserInfo();
      } else {
        // 后端返回失败，使用本地预览
        this.imageUrl = URL.createObjectURL(file.raw);
        this.$message.error('头像上传失败，请重试');
      }
    },
    getStatusType(status) {
      const types = {
        0: 'info',
        1: 'warning',
        2: 'success',
        3: 'danger'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        0: '待审核',
        1: '已通过',
        2: '已完成',
        3: '已取消'
      }
      return texts[status] || '未知'
    },
    payMoney(moneyFlowId) {
      const order = this.moneyFlowList.find(item => item.moneyFlowId === moneyFlowId)
      if (!order) {
        this.$message.error('订单信息不存在')
        return
      }
      if (!order.money || order.money <= 0) {
        this.$message.error('支付金额无效')
        return
      }
      this.currentPayOrder = order
      this.payMethod = ''
      this.payDialogVisible = true
    },
    confirmPay() {
      if (!this.payMethod) {
        this.$message.warning('请选择支付方式')
        return
      }
      if (!this.currentPayOrder) {
        this.$message.error('订单信息不存在')
        return
      }
      if (this.payMethod === '钱包') {
        if (this.userInfo.money < this.currentPayOrder.money) {
          this.$message.error('钱包余额不足，请选择其他支付方式或先充值')
          return
        }
      }
      this.payLoading = true
      const params = {
        userId: this.$store.state.userId,
        moneyFlowId: this.currentPayOrder.moneyFlowId,
        money: this.currentPayOrder.money,
        type: this.payMethod,
        consultantId: this.currentPayOrder.consultantId || null,
      }
      this.$axios.get("/river/pay", { params }).then(res => {
        this.payLoading = false
        if (res.data.code == 1) {
          this.$message.success('支付成功')
          this.payDialogVisible = false
          this.getUserInfo()
          this.getMoneyFlow()
        } else {
          this.$message.error(res.data.msg || '支付失败，请重试')
        }
      }).catch(error => {
        this.payLoading = false
        console.log(error)
        this.$message.error('支付请求失败，请检查网络后重试')
      })
    }
  },
  mounted() {
    if (this.$store.state.userId !== -1) {
      this.getUserInfo()
      this.getReports()
      this.getAppointmentList()
    }
  },
  watch: {
    '$store.state.userId'(newUserId) {
      if (newUserId !== -1) {
        this.getUserInfo()
        this.getReports()
        this.getAppointmentList()
      }
    }
  }
}
</script>

<style scoped>
.selectMenu{
  min-height: 600px;
}

.profile-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.profile-card {
  margin-bottom: 20px;
}

.profile-header {
  text-align: center;
  padding: 20px 0;
}

.profile-header h3 {
  margin: 15px 0 5px 0;
}

.account {
  color: #999;
  font-size: 14px;
}

.profile-stats {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.content-card {
  min-height: 600px;
}

.recharge-section {
  padding: 20px;
}

.current-balance {
  font-size: 18px;
  margin-bottom: 30px;
}

.current-balance span {
  font-size: 32px;
  color: #F56C6C;
  font-weight: bold;
}

.recharge-amount {
  margin-bottom: 20px;
}

.custom-amount {
  margin-bottom: 30px;
}

.label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
}

.recharge-btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
}

.income {
  color: #67C23A;
}

.expense {
  color: #F56C6C;
}

.report-detail h4 {
  color: #333;
  margin: 15px 0 10px 0;
}

.report-detail p {
  margin: 5px 0;
  color: #666;
}

.suggestion-content,
.report-content {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  line-height: 1.6;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
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
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border-radius: 50%;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 50%;
}

.pay-dialog-content {
  padding: 10px 0;
}

.pay-info {
  text-align: center;
  margin-bottom: 10px;
}

.pay-amount {
  font-size: 24px;
  color: #F56C6C;
  font-weight: bold;
}

.pay-methods {
  margin-top: 10px;
}

.pay-radio-group {
  display: flex;
  flex-direction: column;
  width: 100%;
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
</style>
