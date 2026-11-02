<template>
    <div class="ai-management-container">
        <el-card>
            <div slot="header" class="card-header">
                <span>AI 管理</span>
                <div class="header-actions">
                    <el-button type="success" size="small" @click="openDifyLink">
                        <i class="el-icon-link"></i> 前往 Dify 官方
                    </el-button>
                    <el-button type="primary" size="small" @click="openAddDialog">
                        <i class="el-icon-plus"></i> 新增
                    </el-button>
                </div>
            </div>

            <div class="ai-table" v-loading="loading">
                <el-table :data="aiList" style="width: 100%" border stripe>
                    <el-table-column prop="aiId" label="ID" width="80"></el-table-column>
                    <el-table-column prop="difyNumber" label="Dify 工作流编号" min-width="180"></el-table-column>
                    <el-table-column prop="difyLocal" label="Dify 链接" min-width="200">
                        <template slot-scope="scope">
                            <el-link :href="scope.row.difyLocal" target="_blank" v-if="scope.row.difyLocal" type="primary">
                                {{ scope.row.difyLocal }}
                            </el-link>
                            <span v-else>-</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="remarks" label="备注" min-width="150">
                        <template slot-scope="scope">
                            {{ scope.row.remarks || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="启用状态" width="120" align="center">
                        <template slot-scope="scope">
                            <el-switch
                                v-model="scope.row.status"
                                :active-value="2"
                                :inactive-value="0"
                                @change="handleStatusChange(scope.row)">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="创建时间" width="160" align="center">
                        <template slot-scope="scope">
                            {{ formatTime(scope.row.createTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="更新时间" width="160" align="center">
                        <template slot-scope="scope">
                            {{ formatTime(scope.row.updateTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="160" fixed="right" align="center">
                        <template slot-scope="scope">
                            <el-button type="primary" size="mini" @click="editAi(scope.row)">编辑</el-button>
                            <el-button type="danger" size="mini" @click="deleteAi(scope.row.aiId)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div v-if="aiList.length === 0 && !loading" class="empty-state">
                    <el-empty description="暂无 AI 配置数据"></el-empty>
                </div>
            </div>
        </el-card>

        <!-- 新增/编辑 AI 对话框 -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false">
            <el-form :model="aiForm" label-width="140px" :rules="aiRules" ref="aiForm">
                <el-form-item label="Dify 工作流编号" prop="difyNumber">
                    <el-input v-model="aiForm.difyNumber" placeholder="请输入 Dify 工作流编号"></el-input>
                </el-form-item>
                <el-form-item label="Dify 链接" prop="difyLocal">
                    <el-input v-model="aiForm.difyLocal" placeholder="请输入 Dify 链接地址"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="aiForm.remarks" type="textarea" :rows="3" placeholder="请输入备注信息"></el-input>
                </el-form-item>
                <el-form-item label="启用状态">
                    <el-switch
                        v-model="aiForm.status"
                        :active-value="2"
                        :inactive-value="0">
                    </el-switch>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveAi" :loading="saving">保存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            aiList: [],
            dialogVisible: false,
            dialogTitle: '',
            loading: false,
            saving: false,
            aiForm: {
                aiId: null,
                difyNumber: '',
                difyLocal: '',
                remarks: '',
                status: 0
            },
            aiRules: {
                difyNumber: [
                    { required: true, message: '请输入 Dify 工作流编号', trigger: 'blur' }
                ],
                difyLocal: [
                    { required: true, message: '请输入 Dify 链接地址', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        getAiList() {
            this.loading = true
            this.$axios.get("/ai/selectAi").then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.aiList = res.data.data || []
                } else {
                    this.$message({
                        message: res.data.msg || '获取 AI 列表失败',
                        type: 'error'
                    })
                }
            }).catch(error => {
                this.loading = false
                console.error('获取 AI 列表失败:', error)
                this.$message({
                    message: '网络错误，获取 AI 列表失败',
                    type: 'error'
                })
            })
        },
        openAddDialog() {
            this.dialogTitle = '新增 AI 配置'
            this.aiForm = {
                aiId: null,
                difyNumber: '',
                difyLocal: '',
                remarks: '',
                status: 0
            }
            if (this.$refs.aiForm) {
                this.$refs.aiForm.resetFields()
            }
            this.dialogVisible = true
        },
        editAi(ai) {
            this.aiForm = { ...ai }
            this.dialogTitle = '编辑 AI 配置'
            this.dialogVisible = true
        },
        saveAi() {
            if (!this.$refs.aiForm) return
            this.$refs.aiForm.validate((valid) => {
                if (!valid) {
                    return
                }
                this.saving = true
                const url = this.aiForm.aiId ? "/ai/updateAi" : "/ai/insertAi"
                const params = {
                    difyNumber: this.aiForm.difyNumber,
                    difyLocal: this.aiForm.difyLocal,
                    remarks: this.aiForm.remarks,
                    status: this.aiForm.status
                }
                if (this.aiForm.aiId) {
                    params.aiId = this.aiForm.aiId
                }
                this.$axios.get(url, { params }).then(res => {
                    this.saving = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: "保存成功",
                            type: "success"
                        })
                        this.dialogVisible = false
                        this.getAiList()
                    } else {
                        this.$message({
                            message: res.data.msg || "保存失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.saving = false
                    console.error('保存 AI 配置失败:', error)
                    this.$message({
                        message: '网络错误，保存失败',
                        type: 'error'
                    })
                })
            })
        },
        deleteAi(id) {
            this.$confirm('确定要删除此条 AI 配置吗？删除后不可恢复。', '删除确认', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.loading = true
                this.$axios.get("/ai/deleteAi", {
                    params: { id: id }
                }).then(res => {
                    this.loading = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: "删除成功",
                            type: "success"
                        })
                        this.getAiList()
                    } else {
                        this.$message({
                            message: res.data.msg || "删除失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.loading = false
                    console.error('删除 AI 配置失败:', error)
                    this.$message({
                        message: '网络错误，删除失败',
                        type: 'error'
                    })
                })
            }).catch(() => { })
        },
        handleStatusChange(row) {
            if (row.status === 2) {
                this.$confirm(`确定要启用此条 AI 配置吗？启用后，当前启用的 AI 将被禁用。`, '启用确认', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get("/ai/enableAi", {
                        params: { id: row.aiId }
                    }).then(res => {
                        if (res.data.code == 1) {
                            this.$message({
                                message: '启用成功',
                                type: "success"
                            })
                            this.getAiList()
                        } else {
                            this.$message({
                                message: res.data.msg || '启用失败',
                                type: "error"
                            })
                            row.status = 0
                        }
                    }).catch(error => {
                        console.error('启用 AI 配置失败:', error)
                        this.$message({
                            message: '网络错误，操作失败',
                            type: 'error'
                        })
                        row.status = 0
                    })
                }).catch(() => {
                    row.status = 0
                })
            } else {
                this.$message({
                    message: '无法单独禁用，请启用其他 AI 来替换',
                    type: 'warning'
                })
                row.status = 2
            }
        },
        openDifyLink() {
            window.open('https://cloud.dify.ai/apps', '_blank')
        },
        formatTime(time) {
            if (!time) return '-'
            const date = new Date(time)
            const year = date.getFullYear()
            const month = String(date.getMonth() + 1).padStart(2, '0')
            const day = String(date.getDate()).padStart(2, '0')
            const hours = String(date.getHours()).padStart(2, '0')
            const minutes = String(date.getMinutes()).padStart(2, '0')
            return `${year}-${month}-${day} ${hours}:${minutes}`
        }
    },
    mounted() {
        this.getAiList()
    }
}
</script>

<style scoped>
.ai-management-container {
    padding: 20px;
    min-height: 100vh;
    background-color: #f5f7fa;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.header-actions {
    display: flex;
    gap: 10px;
}

.ai-table {
    margin-top: 20px;
    min-height: 400px;
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

.empty-state {
    margin-top: 40px;
    text-align: center;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
    .ai-management-container {
        padding: 10px;
    }

    .ai-table {
        overflow-x: auto;
    }

    .el-table {
        min-width: 800px;
    }
}
</style>
