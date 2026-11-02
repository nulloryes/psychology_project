<template>
    <div class="menu-management-container">
        <el-card>
            <div slot="header" class="card-header">
                <span>菜单管理</span>
                <el-button type="primary" size="small" @click="openAddDialog">
                    <i class="el-icon-plus"></i> 新增菜单
                </el-button>
            </div>

            <div class="menu-table" v-loading="loading">
                <el-table :data="menuList" style="width: 100%" border stripe>
                    <el-table-column prop="menuId" label="菜单ID" width="100"></el-table-column>
                    <el-table-column prop="name" label="菜单名称" min-width="180"></el-table-column>
                    <el-table-column label="上级菜单" min-width="180">
                        <template slot-scope="scope">
                            {{ getParentMenuName(scope.row.upId) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="order" label="排序" width="80" align="center"></el-table-column>
                    <el-table-column label="状态" width="100" align="center">
                        <template slot-scope="scope">
                            <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
                                {{ scope.row.status === 0 ? '启用' : '禁用' }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="180" fixed="right" align="center">
                        <template slot-scope="scope">
                            <el-button type="primary" size="mini" @click="editMenu(scope.row)">编辑</el-button>
                            <el-button :type="scope.row.status === 0 ? 'danger' : 'success'" size="mini"
                                @click="disableMenu(scope.row.menuId, scope.row.status)">
                                {{ scope.row.status === 0 ? '禁用' : '启用' }}
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div v-if="menuList.length === 0 && !loading" class="empty-state">
                    <el-empty description="暂无菜单数据"></el-empty>
                </div>
            </div>
        </el-card>

        <!-- 新增/编辑菜单对话框 -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
            <el-form :model="menuForm" label-width="100px" :rules="menuRules" ref="menuForm">
                <el-form-item label="菜单名称" prop="name">
                    <el-input v-model="menuForm.name" placeholder="请输入菜单名称"></el-input>
                </el-form-item>
                <el-form-item label="上级菜单" prop="upId">
                    <el-select v-model="menuForm.upId" style="width: 100%" placeholder="请选择上级菜单">
                        <el-option label="顶级菜单" value="0"></el-option>
                        <el-option v-for="menu in filteredMenuList" :key="menu.menuId" :label="menu.name"
                            :value="menu.menuId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="排序" prop="order">
                    <el-input v-model.number="menuForm.order" type="number" placeholder="请输入排序值" min="0"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-switch v-model="menuForm.status" active-value="1" inactive-value="0"></el-switch>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveMenu" :loading="saving">保存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            menuList: [],
            menuTree: [],
            dialogVisible: false,
            dialogTitle: '',
            loading: false,
            saving: false,
            currentNodeKey: null,
            menuForm: {
                menuId: null,
                name: '',
                upId: 0,
                order: 0,
                status: 1
            },
            menuRules: {
                name: [
                    { required: true, message: '请输入菜单名称', trigger: 'blur' },
                    { min: 1, max: 50, message: '菜单名称长度在 1 到 50 个字符', trigger: 'blur' }
                ],
                order: [
                    { type: 'number', message: '排序值必须是数字', trigger: 'blur' }
                ]
            }
        }
    },
    computed: {
        filteredMenuList() {
            // 编辑时排除当前菜单，避免选择自己作为上级菜单
            if (!this.menuForm.menuId) {
                return this.menuList
            }
            return this.menuList.filter(menu => menu.menuId !== this.menuForm.menuId)
        }
    },
    methods: {
        getMenus() {
            this.loading = true
            this.$axios.get("/system/selectMenu").then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.menuList = res.data.data || []
                } else {
                    this.$message({
                        message: res.data.msg || "获取菜单失败",
                        type: "error"
                    })
                }
            }).catch(error => {
                this.loading = false
                console.error('获取菜单失败:', error)
                this.$message({
                    message: "网络错误，获取菜单失败",
                    type: "error"
                })
            })
        },
        getParentMenuName(upId) {
            if (upId === 0) {
                return '顶级菜单'
            }
            const parentMenu = this.menuList.find(menu => menu.menuId === upId)
            return parentMenu ? parentMenu.name : '未知'
        },
        handleNodeClick(data) {
            this.currentNodeKey = data.menuId
        },
        openAddDialog() {
            this.dialogTitle = '新增菜单'
            this.menuForm = {
                menuId: null,
                name: '',
                upId: 0,
                order: 0,
                status: 1
            }
            if (this.$refs.menuForm) {
                this.$refs.menuForm.resetFields()
            }
            this.dialogVisible = true
        },
        editMenu(menu) {
            this.menuForm = { ...menu }
            this.dialogTitle = '编辑菜单'
            this.dialogVisible = true
        },
        saveMenu() {
            if (!this.$refs.menuForm) return
            this.$refs.menuForm.validate((valid) => {
                if (!valid) {
                    return
                }
                this.saving = true
                const url = this.menuForm.menuId ? "/system/updateMenu" : "/system/addMenu"
                this.$axios.get(url, {
                    params: {
                        menuId: this.menuForm.menuId,
                        name: this.menuForm.name,
                        upId: this.menuForm.upId,
                        order: this.menuForm.order,
                        status: this.menuForm.status
                    }
                }).then(res => {
                    this.saving = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: "保存成功",
                            type: "success"
                        })
                        this.dialogVisible = false
                        this.getMenus()
                    } else {
                        this.$message({
                            message: res.data.msg || "保存失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.saving = false
                    console.error('保存菜单失败:', error)
                    this.$message({
                        message: "网络错误，保存失败",
                        type: "error"
                    })
                })
            })
        },
        disableMenu(menuId, status) {
            const action = status === 0 ? '禁用' : '启用'
            if(status == 0){
                status = 1 
            }else{
                status = 0
            }
            this.$confirm(`确定要${action}该菜单吗？`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.loading = true
                this.$axios.get("/system/menuStatus", {
                    params: {
                        id: menuId,
                        status: status
                    }
                }).then(res => {
                    this.loading = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: `${action}成功`,
                            type: "success"
                        })
                        this.getMenus()
                    } else {
                        this.$message({
                            message: res.data.msg || `${action}失败`,
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.loading = false
                    console.error(`${action}菜单失败:`, error)
                    this.$message({
                        message: "网络错误，操作失败",
                        type: "error"
                    })
                })
            }).catch(() => { })
        }
    },
    mounted() {
        this.getMenus()
    }
}
</script>

<style scoped>
.menu-management-container {
    padding: 20px;
    min-height: 100vh;
    background-color: #f5f7fa;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.menu-table {
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
    .menu-management-container {
        padding: 10px;
    }

    .menu-table {
        overflow-x: auto;
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
    .el-table-column {
        min-width: 100px;
    }
}
</style>
