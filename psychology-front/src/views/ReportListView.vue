<template>
    <div class="report-list-container">
        <el-card>
            <div slot="header">
                <span>我的心理报告</span>
            </div>

            <div class="report-tabs">
                <el-tabs v-model="activeTab" @tab-click="handleTabChange">
                    <el-tab-pane label="咨询报告" name="consultation">
                        <el-table :data="consultationReports" style="width: 100%" v-loading="loading">
                            <el-table-column prop="consultantName" label="咨询师"></el-table-column>
                            <el-table-column prop="orderTime" label="咨询日期"></el-table-column>
                            <el-table-column prop="createTime" label="报告生成时间">
                                <template slot-scope="scope">
                                    {{ scope.row.createTime | formatTime }}
                                </template>
                            </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                    <el-button type="primary" size="mini"
                                        @click="viewConsultationReport(scope.row)">查看报告</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pagination-container">
                            <el-pagination background layout="prev, pager, next, jumper, sizes, total" :total="total"
                                :page-size="pageSize" :current-page="pageNum" @size-change="handleSizeChange"
                                @current-change="handleCurrentChange" />
                        </div>
                    </el-tab-pane>

                    <el-tab-pane label="在线评估报告" name="evaluation">
                        <el-table :data="evaluationReports" style="width: 100%" v-loading="loading">
                            <el-table-column prop="domainName" label="评估类型"></el-table-column>
                            <el-table-column prop="score" label="得分"></el-table-column>
                            <el-table-column prop="tip" label="评估结果"></el-table-column>
                            <el-table-column prop="createTime" label="完成时间">
                                <template slot-scope="scope">
                                    {{ scope.row.createTime | formatTime }}
                                </template>
                            </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                    <el-button type="primary" size="mini"
                                        @click="viewEvaluationReport(scope.row)">查看详情</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pagination-container">
                            <el-pagination background layout="prev, pager, next, jumper, sizes, total" :total="total"
                                :page-size="pageSize" :current-page="pageNum" @size-change="handleSizeChange"
                                @current-change="handleCurrentChange" />
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </div>
        </el-card>

        <!-- 咨询报告详情弹窗 -->
        <el-dialog title="咨询报告详情" :visible.sync="consultationDialogVisible" width="600px">
            <div v-if="currentConsultationReport.reservationId" class="report-detail">
                <h4>基本信息</h4>
                <p><strong>咨询师：</strong>{{ currentConsultationReport.consultantName }}</p>
                <p><strong>咨询日期：</strong>{{ currentConsultationReport.orderTime }}</p>
                <p><strong>咨询领域：</strong>{{ currentConsultationReport.domainName }}</p>
                <el-divider></el-divider>
                <h4>咨询报告</h4>
                <div class="report-content">{{ currentConsultationReport.problem || '暂无报告内容' }}</div>
                <el-divider></el-divider>
                <h4>专家建议</h4>
                <div class="suggestion-content">{{ currentConsultationReport.answer || '暂无建议' }}</div>
            </div>
        </el-dialog>

        <!-- 评估报告详情弹窗 -->
        <el-dialog title="评估报告详情" :visible.sync="evaluationDialogVisible" width="600px">
            <div v-if="currentEvaluationReport" class="report-detail">
                <h4>评估信息</h4>
                <p><strong>评估类型：</strong>{{ currentEvaluationReport.domainName || '暂无' }}</p>
                <p><strong>完成时间：</strong>{{ currentEvaluationReport.createTime | formatTime }}</p>
                <p><strong>得分：</strong>{{ currentEvaluationReport.score || '暂无' }}</p>
                <el-divider></el-divider>
                <h4>评估结果</h4>
                <div class="result-content">{{ currentEvaluationReport.tip || '暂无评估结果' }}</div>
                <el-divider v-if="currentEvaluationReport.report && (Array.isArray(currentEvaluationReport.report) ? currentEvaluationReport.report.length > 0 : true)"></el-divider>
                <div v-if="currentEvaluationReport.report">
                    <template v-if="Array.isArray(currentEvaluationReport.report)">
                        <div v-for="(report, index) in currentEvaluationReport.report" :key="index" class="expert-report">
                            <h4>专家答复 {{ index + 1 }}: {{ report.consultantName || '匿名专家' }}</h4>
                            <div class="answer-content">{{ report.result || '暂无答复内容' }}</div>
                            <el-divider v-if="index < currentEvaluationReport.report.length - 1"></el-divider>
                        </div>
                    </template>
                    <template v-else>
                        <h4>专家答复: {{ currentEvaluationReport.report.consultantName || '匿名专家' }}</h4>
                        <div class="answer-content">{{ currentEvaluationReport.report.result || '暂无答复内容' }}</div>
                    </template>
                </div>
                <div v-else>
                    <p>暂无专家答复</p>
                </div>
            </div>
            <div v-else>
                <p>报告信息加载中...</p>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            activeTab: 'consultation',
            consultationReports: [],
            evaluationReports: [],
            loading: false,
            consultationDialogVisible: false,
            evaluationDialogVisible: false,
            currentConsultationReport: {},
            currentEvaluationReport: {},
            // 分页相关变量
            pageNum: 1,
            pageSize: 10,
            total: 0
        }
    },
    methods: {
        getConsultationReports() {
            this.loading = true
            this.$axios.get("/consultation/selectReservation", {
                params: {
                    userId: this.$store.state.userId,
                    status: 2,
                    pageNum: this.pageNum,
                    pageSize: this.pageSize
                }
            }).then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.consultationReports = res.data.data.list
                    this.total = res.data.data.total
                }
            }).catch(error => {
                this.loading = false
                console.log(error)
            })
        },
        getEvaluationReports() {
            this.loading = true
            this.$axios.get("/question/selectMyReport", {
                params: {
                    userId: this.$store.state.userId,
                    pageNum: this.pageNum,
                    pageSize: this.pageSize
                }
            }).then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.evaluationReports = res.data.data.list
                    this.total = res.data.data.total
                }
                console.log(res)
            }).catch(error => {
                this.loading = false
                console.log(error)
            })
        },
        handleTabChange(tab) {
            this.pageNum = 1 // 切换标签页时重置页码
            if (tab.name === 'consultation') {
                this.getConsultationReports()
            } else {
                this.getEvaluationReports()
            }
        },
        viewConsultationReport(row) {
            this.currentConsultationReport = row
            this.consultationDialogVisible = true
        },
        viewEvaluationReport(row) {
            this.currentEvaluationReport = row
            this.evaluationDialogVisible = true
        },
        // 分页方法
        handleSizeChange(val) {
            this.pageSize = val
            if (this.activeTab === 'consultation') {
                this.getConsultationReports()
            } else {
                this.getEvaluationReports()
            }
        },
        handleCurrentChange(val) {
            this.pageNum = val
            if (this.activeTab === 'consultation') {
                this.getConsultationReports()
            } else {
                this.getEvaluationReports()
            }
        }
    },
    mounted() {
        if (this.$store.state.userId !== -1) {
            this.getConsultationReports()
        }
    },
    watch: {
        '$store.state.userId'(newUserId) {
            if (newUserId !== -1) {
                this.getConsultationReports()
            }
        }
    }
}
</script>

<style scoped>
.report-list-container {
    padding: 20px;
}

.report-detail h4 {
    color: #333;
    margin: 15px 0 10px 0;
}

.report-detail p {
    margin: 5px 0;
    color: #666;
}

.report-content,
.suggestion-content,
.result-content,
.answer-content {
    background: #f5f7fa;
    padding: 15px;
    border-radius: 4px;
    line-height: 1.6;
    color: #666;
}

.pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
}

.expert-report {
    margin-bottom: 20px;
}

.expert-report:last-child {
    margin-bottom: 0;
}
</style>
