<template>
    <div class="question-bank-container">
        <el-card>
            <div slot="header">
                <span>题库管理</span>
                <el-button type="primary" size="small" @click="openAddDialog" style="float: right;">
                    <i class="el-icon-plus"></i> 新增题目
                </el-button>
            </div>

            <div class="search-bar">
                <el-input placeholder="搜索题目" v-model="searchQuery" class="search-input" clearable>
                    <el-button slot="append" icon="el-icon-search" @click="searchQuestions"></el-button>
                </el-input>
                <el-select v-model="reportTypeId" placeholder="选择评估类型" clearable @change="searchQuestions">
                    <el-option v-for="type in reportTypeList" :key="type.domainId" :label="type.name"
                        :value="type.domainId"></el-option>
                </el-select>
                <el-button type="primary" size="small" @click="resetSearch">重置</el-button>
            </div>

            <el-table :data="questionList" style="width: 100%" v-loading="loading">
                <el-table-column prop="questionId" label="题目ID" width="80"></el-table-column>
                <el-table-column prop="question" label="题目内容" width="400" align="center"></el-table-column>
                <el-table-column label="评估类型"  align="center">
                    <template slot-scope="scope">
                        {{ getDomainText(scope.row.domainId) }}
                    </template>
                </el-table-column>
                <el-table-column label="创建时间">
                    <template slot-scope="scope">
                        {{ scope.row.createTime | formatTime }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="300">
                    <template slot-scope="scope">
                        <el-button type="primary" size="small" @click="viewQuestion(scope.row)">查看</el-button>
                        <el-button type="success" size="small" @click="editQuestion(scope.row)">编辑</el-button>
                        <el-button type="danger" size="small" @click="deleteQuestion(scope.row.questionId)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
                layout="prev, pager, next, total" :total="total">
            </el-pagination>
        </el-card>

        <!-- 新增/编辑题目对话框 -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false">
            <el-form :model="questionForm" label-width="100px" :rules="questionRules" ref="questionForm">
                <el-form-item label="题目描述" prop="question">
                    <el-input type="textarea" v-model="questionForm.question" :rows="3" placeholder="请输入题目内容"></el-input>
                </el-form-item>
                <el-form-item label="评估类型" prop="domainId">
                    <el-select v-model="questionForm.domainId" style="width: 100%" placeholder="请选择评估类型">
                        <el-option v-for="type in reportTypeList" :key="type.domainId" :label="type.name"
                            :value="type.domainId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="选项A" prop="aq">
                    <div class="option-item">
                        <el-input v-model="questionForm.aq" placeholder="选项内容" style="width: 300px; margin-right: 10px;"></el-input>
                        <el-input v-model.number="questionForm.as" placeholder="分值" type="number" style="width: 100px;"></el-input>
                    </div>
                </el-form-item>
                <el-form-item label="选项B" prop="bq">
                    <div class="option-item">
                        <el-input v-model="questionForm.bq" placeholder="选项内容" style="width: 300px; margin-right: 10px;"></el-input>
                        <el-input v-model.number="questionForm.bs" placeholder="分值" type="number" style="width: 100px;"></el-input>
                    </div>
                </el-form-item>
                <el-form-item label="选项C" prop="cq">
                    <div class="option-item">
                        <el-input v-model="questionForm.cq" placeholder="选项内容" style="width: 300px; margin-right: 10px;"></el-input>
                        <el-input v-model.number="questionForm.cs" placeholder="分值" type="number" style="width: 100px;"></el-input>
                    </div>
                </el-form-item>
                <el-form-item label="选项D" prop="dq">
                    <div class="option-item">
                        <el-input v-model="questionForm.dq" placeholder="选项内容" style="width: 300px; margin-right: 10px;"></el-input>
                        <el-input v-model.number="questionForm.ds" placeholder="分值" type="number" style="width: 100px;"></el-input>
                    </div>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveQuestion" :loading="saving">保存</el-button>
            </div>
        </el-dialog>

        <!-- 查看题目详情对话框 -->
        <el-dialog title="查看题目" :visible.sync="infoVisible" width="600px">
            <el-descriptions :column="1" border>
                <el-descriptions-item label="题目内容">{{ questionForm.question }}</el-descriptions-item>
                <el-descriptions-item label="评估类型">{{ getDomainText(questionForm.domainId) }}</el-descriptions-item>
                <el-descriptions-item label="选项A">{{ questionForm.aq }} ({{ questionForm.as }}分)</el-descriptions-item>
                <el-descriptions-item label="选项B">{{ questionForm.bq }} ({{ questionForm.bs }}分)</el-descriptions-item>
                <el-descriptions-item label="选项C">{{ questionForm.cq }} ({{ questionForm.cs }}分)</el-descriptions-item>
                <el-descriptions-item label="选项D">{{ questionForm.dq }} ({{ questionForm.ds }}分)</el-descriptions-item>
                <el-descriptions-item label="创建时间">
                    {{ questionForm.createTime | formatTime }}
                </el-descriptions-item>
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
            questionList: [],
            reportTypeList: [],
            loading: false,
            saving: false,
            currentPage: 1,
            pageSize: 10,
            total: 0,
            searchQuery: '',
            reportTypeId: null,
            dialogVisible: false,
            infoVisible: false,
            dialogTitle: '',
            questionForm: {
                questionId: null,
                domainId: null,
                question: '',
                aq: '',
                as: '',
                bq: '',
                bs: '',
                cq: '',
                cs: '',
                dq: '',
                ds: ''
            },
            questionRules: {
                question: [
                    { required: true, message: '请输入题目内容', trigger: 'blur' },
                    { min: 1, max: 200, message: '题目内容长度在 1 到 200 个字符', trigger: 'blur' }
                ],
                domainId: [
                    { required: true, message: '请选择评估类型', trigger: 'blur' }
                ],
                aq: [
                    { required: true, message: '请输入选项A内容', trigger: 'blur' }
                ],
                as: [
                    { required: true, message: '请输入选项A分值', trigger: 'blur' },
                    { type: 'number', message: '分值必须是数字', trigger: 'blur' }
                ],
                bq: [
                    { required: true, message: '请输入选项B内容', trigger: 'blur' }
                ],
                bs: [
                    { required: true, message: '请输入选项B分值', trigger: 'blur' },
                    { type: 'number', message: '分值必须是数字', trigger: 'blur' }
                ],
                cq: [
                    { required: true, message: '请输入选项C内容', trigger: 'blur' }
                ],
                cs: [
                    { required: true, message: '请输入选项C分值', trigger: 'blur' },
                    { type: 'number', message: '分值必须是数字', trigger: 'blur' }
                ],
                dq: [
                    { required: true, message: '请输入选项D内容', trigger: 'blur' }
                ],
                ds: [
                    { required: true, message: '请输入选项D分值', trigger: 'blur' },
                    { type: 'number', message: '分值必须是数字', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        getQuestions() {
            this.loading = true
            this.$axios.get("/question/selectQuestion", {
                params: {
                    question: this.searchQuery,
                    domainId: this.reportTypeId,
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            }).then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.questionList = res.data.data.list || []
                    this.total = res.data.data.total || 0
                }
            }).catch(error => {
                this.loading = false
                console.log(error)
            })
        },
        getReportTypes() {
            this.$axios.get("/domain/allDomain").then(res => {
                if (res.data.code == 1) {
                    this.reportTypeList = res.data.data || []
                }
            }).catch(error => {
                console.log(error)
            })
        },
        searchQuestions() {
            this.currentPage = 1
            this.getQuestions()
        },
        resetSearch() {
            this.searchQuery = ''
            this.reportTypeId = null
            this.currentPage = 1
            this.getQuestions()
        },
        handleCurrentChange(val) {
            this.currentPage = val
            this.getQuestions()
        },
        openAddDialog() {
            this.dialogTitle = '新增题目'
            this.questionForm = {
                questionId: null,
                domainId: null,
                question: '',
                aq: '',
                as: '',
                bq: '',
                bs: '',
                cq: '',
                cs: '',
                dq: '',
                ds: ''
            }
            if (this.$refs.questionForm) {
                this.$refs.questionForm.resetFields()
            }
            this.dialogVisible = true
        },
        viewQuestion(question) {
            this.$axios.get("/question/selectQuestion", {
                params: {
                    questionId: question.questionId
                }
            }).then(res => {
                console.log(res)
                if (res.data.code == 1) {
                    this.questionForm = res.data.data.list[0]
                    this.infoVisible = true
                }
            }).catch(error => {
                console.error('获取题目失败:', error)
                this.$message({
                    message: "网络错误，获取题目失败",
                    type: "error"
                })
            })
        },
        editQuestion(question) {
            this.questionForm = { ...question }
            this.dialogTitle = '编辑题目'
            this.dialogVisible = true
        },
        saveQuestion() {
            if (!this.$refs.questionForm) return
            this.$refs.questionForm.validate((valid) => {
                if (!valid) {
                    return
                }
                this.saving = true
                const url = this.questionForm.questionId ? "/question/updateQuestion" : "/question/insertQuestion"
                this.$axios.get(url, {
                    params:{
                        questionId: this.questionForm.questionId,
                        question: this.questionForm.question,
                        domainId: this.questionForm.domainId,
                        aq: this.questionForm.aq,
                        as: this.questionForm.as,
                        bq: this.questionForm.bq,
                        bs: this.questionForm.bs,
                        cq: this.questionForm.cq,
                        cs: this.questionForm.cs,
                        dq: this.questionForm.dq,
                        ds: this.questionForm.ds
                    }
                }).then(res => {
                    this.saving = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: "保存成功",
                            type: "success"
                        })
                        this.dialogVisible = false
                        this.getQuestions()
                    } else {
                        this.$message({
                            message: res.data.msg || "保存失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.saving = false
                    console.error('保存题目失败:', error)
                    this.$message({
                        message: "网络错误，保存失败",
                        type: "error"
                    })
                })
            })
        },
        deleteQuestion(questionId) {
            this.$confirm('确定要删除该题目吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.loading = true
                this.$axios.get("/question/deleteQuestion", {
                    params:{
                        id: questionId
                    }
                }).then(res => {
                    this.loading = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: "删除成功",
                            type: "success"
                        })
                        this.getQuestions()
                    } else {
                        this.$message({
                            message: res.data.msg || "删除失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.loading = false
                    console.error('删除题目失败:', error)
                    this.$message({
                        message: "网络错误，删除失败",
                        type: "error"
                    })
                })
            }).catch(() => { })
        },
        getDomainText(id) {
            const item = this.reportTypeList.find(item => item.domainId === id);
            return item ? item.name : '未知'
        },
    },
    mounted() {
        this.getQuestions()
        this.getReportTypes()
    }
}
</script>

<style scoped>
.question-bank-container {
    padding: 20px;
    min-height: 100vh;
    background-color: #f5f7fa;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.search-bar {
    margin-bottom: 20px;
    display: flex;
    gap: 10px;
    align-items: center;
    flex-wrap: wrap;
}

.search-input {
    width: 300px;
}

.option-item {
    margin-bottom: 10px;
    display: flex;
    align-items: center;
}

.el-table {
    border-radius: 4px;
    overflow: hidden;
}

.el-table th {
    background-color: #f5f7fa;
    font-weight: 600;
}

.el-table tr:hover {
    background-color: rgba(0, 0, 0, 0.02);
}

.el-table .el-button {
    margin-right: 8px;
}

.el-table .el-button:last-child {
    margin-right: 0;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
    .question-bank-container {
        padding: 10px;
    }

    .search-bar {
        flex-direction: column;
        align-items: stretch;
    }

    .search-input {
        width: 100%;
    }

    .el-table {
        min-width: 600px;
    }

    .el-table .el-button {
        margin-right: 4px;
        font-size: 12px;
        padding: 4px 8px;
    }
}

/* 适配中等屏幕 */
@media screen and (max-width: 1024px) {
    .search-input {
        width: 200px;
    }
}
</style>
