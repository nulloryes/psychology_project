<template>
    <div class="consultation-diagnosis-container">
        <el-card>
            <div slot="header" class="card-header">
                <span>咨询诊断</span>
                <el-button type="primary" size="small" @click="goBack">返回列表</el-button>
            </div>

            <div class="diagnosis-content" v-if="consultation.reservationId" v-loading="loading">
                <!-- 基本信息 -->
                <el-descriptions :column="2" border class="basic-info">
                    <el-descriptions-item label="预约ID">{{ consultation.reservationId }}</el-descriptions-item>
                    <el-descriptions-item label="用户姓名">{{ consultation.userName }}</el-descriptions-item>
                    <el-descriptions-item label="咨询类型">{{ consultation.domainName }}</el-descriptions-item>
                    <el-descriptions-item label="预约日期">{{ consultation.orderTime }}</el-descriptions-item>
                </el-descriptions>

                <el-divider></el-divider>

                <!-- 问题描述 -->
                <el-card class="problem-card">
                    <template slot="header">
                        <div class="card-title">
                            <i class="el-icon-question-circle"></i>
                            <span>问题描述</span>
                        </div>
                    </template>
                    <div class="problem-content">
                        {{ consultation.problem || '暂无描述' }}
                    </div>
                </el-card>

                <el-divider></el-divider>

                <!-- 咨询师回答 -->
                <el-card class="answer-card">
                    <template slot="header">
                        <div class="card-title">
                            <i class="el-icon-edit"></i>
                            <span>咨询师回答</span>
                        </div>
                    </template>
                    <el-form :model="answerForm" :rules="answerRules" ref="answerForm" label-width="80px">
                        <el-form-item label="回答内容" prop="answer">
                            <el-input type="textarea" v-model="answerForm.answer" :rows="6" placeholder="请输入您的回答内容..."
                                resize="vertical"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitAnswer" :loading="submitting">
                                {{ submitting ? '提交中...' : '提交回答' }}
                            </el-button>
                            <el-button @click="resetForm">重置</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>
            </div>

            <div v-else class="empty-state">
                <el-empty description="请选择要诊断的咨询"></el-empty>
                <el-button type="primary" style="margin-top: 20px" @click="goBack">返回列表</el-button>
            </div>
        </el-card>
    </div>
</template>

<script>
export default {
    data() {
        return {
            consultation: {},
            loading: false,
            submitting: false,
            answerForm: {
                answer: ''
            },
            answerRules: {
                answer: [
                    { required: true, message: '请输入回答内容', trigger: 'blur' },
                    { min: 10, message: '回答内容至少10个字符', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        // 获取预约详情
        getConsultation() {
            const id = this.$route.query.id
            if (id) {
                this.loading = true
                this.$axios.get("/consultation/selectReservationById", {
                    params: {
                        reservationId: id
                    }
                }).then(res => {
                    this.loading = false
                    if (res.data.code == 1) {
                        this.consultation = res.data.data
                        // 如果已有回答，填充到表单
                        if (res.data.data.answer) {
                            this.answerForm.answer = res.data.data.answer
                        }
                    } else {
                        this.$message({
                            message: res.data.msg || "获取预约详情失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.loading = false
                    console.error('获取预约详情失败:', error)
                    this.$message({
                        message: "网络错误，获取预约详情失败",
                        type: "error"
                    })
                })
            }
        },
        // 提交回答
        submitAnswer() {
            if (!this.$refs.answerForm) return
            this.$refs.answerForm.validate((valid) => {
                if (!valid) {
                    return
                }
                this.submitting = true
                this.$axios.get("/consultation/updateReservation", {
                    params: {
                        reservationId: this.consultation.reservationId,
                        consultantId: this.consultation.consultantId,
                        userId: this.consultation.userId,
                        answer: this.answerForm.answer,
                        status: 2
                    }
                }).then(res => {
                    this.submitting = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: "回答提交成功",
                            type: "success"
                        })
                        this.$router.push('/main/consultation-list')
                    } else {
                        this.$message({
                            message: res.data.msg || "回答提交失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.submitting = false
                    console.error('提交回答失败:', error)
                    this.$message({
                        message: "网络错误，提交回答失败",
                        type: "error"
                    })
                })
            })
        },
        // 重置表单
        resetForm() {
            if (this.$refs.answerForm) {
                this.$refs.answerForm.resetFields()
            }
        },
        goBack() {
            this.$router.push('/main/consultation-list')
        },
    },
    mounted() {
        this.getConsultation()
    }
}
</script>

<style scoped>
.consultation-diagnosis-container {
    padding: 20px;
    min-height: 100vh;
    background-color: #f5f7fa;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.basic-info {
    margin-bottom: 20px;
}

.problem-card,
.answer-card {
    margin-bottom: 20px;
}

.card-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
    color: #333;
}

.problem-content {
    padding: 15px;
    background-color: #f9f9f9;
    border-radius: 4px;
    line-height: 1.6;
    color: #666;
    min-height: 100px;
}

.empty-state {
    padding: 100px 0;
    text-align: center;
}
</style>
