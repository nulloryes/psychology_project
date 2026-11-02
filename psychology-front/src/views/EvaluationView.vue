<template>
    <div class="evaluation-container">
        <el-card v-if="!isEvaluating && !showResult">
            <div slot="header">
                <span>在线心理评估</span>
            </div>
            <div class="evaluation-intro">
                <p>选择您想要进行的心理评估类型，系统将为您呈现相应的评估问卷。</p>

                <div v-if="error" class="error-container">
                    <el-alert :title="error" type="error" show-icon :closable="false" />
                    <el-button type="primary" @click="getReportTypes" style="margin-top: 20px">
                        重试
                    </el-button>
                </div>

                <div v-else class="evaluation-types">
                    <el-card v-for="type in reportTypes" :key="type.domainId" class="type-card" shadow="hover"
                        @click.native="startEvaluation(type)">
                        <h4>{{ type.name }}</h4>
                        <el-button type="primary" size="small">开始评估</el-button>
                    </el-card>
                    <div v-if="reportTypes.length === 0" class="empty-container">
                        <p>暂无评估类型</p>
                    </div>
                </div>
            </div>
        </el-card>

        <el-card v-if="isEvaluating" class="question-card">
            <div slot="header">
                <span>{{ currentReportType.name }} - 第 {{ currentQuestionIndex + 1 }} 题 / 共 {{ questions.length }}
                    题</span>
                <el-progress :percentage="progressPercentage" class="progress-bar"></el-progress>
            </div>

            <div v-if="error" class="error-container">
                <el-alert :title="error" type="error" show-icon :closable="false" />
                <el-button type="primary" @click="goBack" style="margin-top: 20px">
                    返回
                </el-button>
            </div>

            <div v-else class="question-content">
                <div v-if="currentQuestion.questionId">
                    <h3>{{ currentQuestion.question }}</h3>

                    <el-radio-group v-model="currentAnswer" class="options-group">
                        <el-radio v-if="currentQuestion.aq" :key="'A'" :label="currentQuestion.as" class="option-item">
                            {{ currentQuestion.aq }}
                        </el-radio>
                        <el-radio v-if="currentQuestion.bq" :key="'B'" :label="currentQuestion.bs" class="option-item">
                            {{ currentQuestion.bq }}
                        </el-radio>
                        <el-radio v-if="currentQuestion.cq" :key="'C'" :label="currentQuestion.cs" class="option-item">
                            {{ currentQuestion.cq }}
                        </el-radio>
                        <el-radio v-if="currentQuestion.dq" :key="'D'" :label="currentQuestion.ds" class="option-item">
                            {{ currentQuestion.dq }}
                        </el-radio>
                    </el-radio-group>
                </div>
                <div v-else>
                    <p>题目加载中...</p>
                </div>
            </div>

            <div class="question-actions">
                <el-button @click="prevQuestion" :disabled="currentQuestionIndex === 0">上一题</el-button>
                <el-button type="primary" @click="nextQuestion" :disabled="currentAnswer === null">
                    {{ currentQuestionIndex === questions.length - 1 ? '提交' : '下一题' }}
                </el-button>
            </div>
        </el-card>

        <el-card v-if="showResult" class="result-card">
            <div slot="header">
                <span>评估结果</span>
            </div>
            <div class="result-content">
                <div class="score-display">
                    <h2>您的得分：{{ evaluationResult.score }}</h2>
                    <el-progress type="circle" :percentage="resultPercentage" :color="resultColor"></el-progress>
                </div>
                <div class="result-detail">
                    <h4>评估结果</h4>
                    <p>{{ evaluationResult.result }}</p>
                </div>
                <div class="suggestion" v-if="evaluationResult.suggestion">
                    <h4>建议</h4>
                    <p>{{ evaluationResult.suggestion }}</p>
                </div>
                <div class="result-actions">
                    <el-button @click="goBack">返回评估列表</el-button>
                    <el-button type="primary" @click="restartEvaluation">重新评估</el-button>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
export default {
    data() {
        return {
            reportTypes: [],
            isEvaluating: false,
            showResult: false,
            currentReportType: {},
            questions: [],
            currentQuestionIndex: 0,
            currentAnswer: null,
            answers: {},
            evaluationResult: {},
            error: ''
        }
    },
    computed: {
        currentQuestion() {
            return this.questions[this.currentQuestionIndex] || {}
        },
        progressPercentage() {
            if (this.questions.length === 0) return 0
            return Math.round((this.currentQuestionIndex / this.questions.length) * 100)
        },
        resultPercentage() {
            if (!this.evaluationResult.score) return 0
            return Math.min(100, Math.round((this.evaluationResult.score / 100) * 100))
        },
        resultColor() {
            const score = this.evaluationResult.score || 0
            if (score > 80) return '#67C23A'
            if (score > 60) return '#E6A23C'
            if (score > 30) return '#909399'
            return '#F56C6C'
        }
    },
    methods: {
        getReportTypes() {
            this.error = ''
            this.$axios.get("/domain/allDomain").then(res => {
                if (res.data.code == 1) {
                    this.reportTypes = res.data.data || []
                } else {
                    this.error = res.data.msg || '获取评估类型失败'
                }
            }).catch(error => {
                this.error = '网络错误，请稍后重试'
                console.log(error)
            })
        },
        startEvaluation(type) {
            this.$confirm('做完问卷之后专家会提供康复建议, 但需花费【50】元 ,是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.currentReportType = type
                this.getQuestions(type.domainId)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });

        },
        getQuestions(reportTypeId) {
            this.error = ''
            this.$axios.get("/question/selectQuestion", {
                params: {
                    domainId: reportTypeId
                }
            }).then(res => {
                if (res.data.code == 1) {
                    console.log(res)
                    this.questions = res.data.data.list || []
                    if (this.questions.length > 0) {
                        this.isEvaluating = true
                        this.currentQuestionIndex = 0
                        this.currentAnswer = null
                        this.answers = {}
                    } else {
                        this.error = '该评估类型暂无题目'
                    }
                } else {
                    this.error = res.data.msg || '获取题目失败'
                }
            }).catch(error => {
                this.error = '网络错误，请稍后重试'
                console.log(error)
            })
        },
        prevQuestion() {
            if (this.currentQuestionIndex > 0) {
                this.currentQuestionIndex--
                this.currentAnswer = this.answers[this.currentQuestion.questionId] || null
            }
        },
        nextQuestion() {
            if (this.currentAnswer === null) {
                this.$message({
                    message: "请选择答案",
                    type: "warning"
                })
                return
            }

            this.answers[this.currentQuestion.questionId] = this.currentAnswer

            if (this.currentQuestionIndex < this.questions.length - 1) {
                this.currentQuestionIndex++
                this.currentAnswer = this.answers[this.questions[this.currentQuestionIndex].questionId] || null
            } else {
                this.submitEvaluation()
            }
        },
        submitEvaluation() {
            if (this.$store.state.userId === -1) {
                this.$message({
                    message: "请先登录",
                    type: "warning"
                })
                this.$router.push("/login")
                return
            }

            this.error = ''

            const totalScore = Object.values(this.answers).reduce((sum, val) => sum + parseInt(val), 0)
            const maxScore = this.questions.length * 100
            const normalizedScore = Math.round((totalScore / maxScore) * 100)

            let result = ''
            let suggestion = ''

            if (normalizedScore < 30) {
                result = '您的心理状态需要引起重视，强烈建议寻求专业帮助。'
                suggestion = '建议：请尽快联系专业心理咨询师或心理医生，获得专业的诊断和治疗。'
            } else if (normalizedScore < 60) {
                result = '您的心理压力较大，建议寻求专业帮助。'
                suggestion = '建议：建议预约专业心理咨询师进行面对面咨询，及时处理心理问题。'
            } else if (normalizedScore < 80) {
                result = '您可能存在轻度的心理压力，建议关注自己的情绪变化。'
                suggestion = '建议：尝试放松技巧如深呼吸、冥想，必要时可寻求专业心理咨询。'
            } else {
                result = '您的心理状态良好，请继续保持积极的生活态度。'
                suggestion = '建议：保持规律的作息，适当运动，与家人朋友保持良好的社交关系。'
            }

            this.$axios.get("/question/insertMyReport", {
                params: {
                    userId: this.$store.state.userId,
                    domainId: this.currentReportType.domainId,
                    score: normalizedScore,
                    tip: result,
                    money: 50,
                    answers: JSON.stringify(this.answers)
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.evaluationResult = {
                        score: normalizedScore,
                        result: result,
                        suggestion: suggestion
                    }
                    this.isEvaluating = false
                    this.showResult = true
                    this.$message({
                        message: "评估完成",
                        type: "success"
                    })
                } else {
                    this.error = res.data.msg || '提交评估失败'
                    this.$message({
                        message: this.error,
                        type: "error"
                    })
                }
            }).catch(error => {
                this.error = '网络错误，请稍后重试'
                this.$message({
                    message: this.error,
                    type: "error"
                })
                console.log(error)
            })
        },
        goBack() {
            this.showResult = false
            this.isEvaluating = false
            this.currentReportType = {}
            this.questions = []
            this.answers = {}
            this.currentQuestionIndex = 0
            this.currentAnswer = null
        },
        restartEvaluation() {
            this.showResult = false
            this.isEvaluating = true
            this.currentQuestionIndex = 0
            this.currentAnswer = null
            this.answers = {}
        }
    },
    mounted() {
        this.getReportTypes()
    }
}
</script>

<style scoped>
.evaluation-container {
    padding: 20px;
    max-width: 1000px;
    margin: 0 auto;
}

.evaluation-intro {
    text-align: center;
}

.evaluation-types {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 20px;
    margin-top: 30px;
}

.type-card {
    cursor: pointer;
    text-align: center;
    transition: transform 0.3s;
}

.type-card:hover {
    transform: translateY(-5px);
}

.type-card h4 {
    margin: 0 0 10px 0;
    color: #333;
}

.type-card p {
    color: #666;
    font-size: 14px;
    margin-bottom: 15px;
}

.question-card {
    max-width: 800px;
    margin: 0 auto;
}

.progress-bar {
    margin-top: 10px;
}

.question-content {
    padding: 20px 0;
}

.question-content h3 {
    margin-bottom: 10px;
    color: #333;
}

.question-desc {
    color: #666;
    margin-bottom: 20px;
}

.options-group {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.option-item {
    padding: 15px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    transition: all 0.3s;
}

.option-item:hover {
    background: #f5f7fa;
}

.question-actions {
    display: flex;
    justify-content: space-between;
    margin-top: 30px;
}

.result-card {
    max-width: 600px;
    margin: 0 auto;
    text-align: center;
}

.score-display {
    margin: 30px 0;
}

.score-display h2 {
    margin-bottom: 20px;
}

.result-detail,
.suggestion {
    margin: 20px 0;
    text-align: left;
}

.result-detail h4,
.suggestion h4 {
    color: #333;
    margin-bottom: 10px;
}

.result-detail p,
.suggestion p {
    color: #666;
    line-height: 1.6;
}

.result-actions {
    margin-top: 30px;
    display: flex;
    justify-content: center;
    gap: 20px;
}



.error-container {
    padding: 40px 0;
    text-align: center;
}

.empty-container {
    text-align: center;
    padding: 40px 0;
    color: #999;
}
</style>
