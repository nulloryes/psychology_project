<template>
    <div class="user-management-container">
        <el-card>
            <div slot="header">
                <span>用户管理</span>
            </div>

            <div class="search-bar">
                <el-input placeholder="搜索用户名" v-model="searchQuery" class="search-input">
                    <el-button slot="append" icon="el-icon-search" @click="searchUsers"></el-button>
                </el-input>
                <el-button type="primary" size="small" @click="resetSearch">重置</el-button>
            </div>

            <el-table :data="userList" style="width: 100%" v-loading="loading">
                <el-table-column prop="userId" label="用户ID" width="80"></el-table-column>
                <el-table-column prop="account" label="账户"></el-table-column>
                <el-table-column prop="name" label="姓名"></el-table-column>
                <el-table-column prop="age" label="年龄"></el-table-column>
                <el-table-column prop="sex" label="性别"></el-table-column>
                <el-table-column prop="money" label="余额"></el-table-column>
                <el-table-column label="创建时间" width="250">
                    <template slot-scope="scope">
                        {{ scope.row.createTime | formatTime }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="250">
                    <template slot-scope="scope">
                        <el-button type="primary" size="small" @click="viewUser(scope.row)">查看</el-button>
                        <el-button type="danger" size="small" @click="deleteUser(scope.row.userId)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
                layout="prev, pager, next, total" :total="total">
            </el-pagination>
        </el-card>

        <el-dialog title="查看用户" :visible.sync="infoVisible" width="600px">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="账户">{{ this.userForm.account }}</el-descriptions-item>
                <el-descriptions-item label="姓名">{{ this.userForm.name }}</el-descriptions-item>
                <el-descriptions-item label="年龄">{{ this.userForm.age }}</el-descriptions-item>
                <el-descriptions-item label="性别">{{ this.userForm.sex }}</el-descriptions-item>
                <el-descriptions-item label="电话号">{{ this.userForm.phone }}</el-descriptions-item>
                <el-descriptions-item label="余额">{{ this.userForm.money }}</el-descriptions-item>
                <el-descriptions-item label="住址">{{ this.userForm.address }}</el-descriptions-item>
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
            userList: [],
            loading: false,
            currentPage: 1,
            pageSize: 10,
            total: 0,
            searchQuery: '',
            infoVisible: false,
            dialogTitle: '',
            userForm: {}
        }
    },
    methods: {
        getUsers() {
            this.loading = true
            this.$axios.get("/user/allUser", {
                params: {
                    name: this.searchQuery,
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            }).then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.userList = res.data.data.list || []
                    this.total = res.data.data.total || 0
                }
            }).catch(error => {
                this.loading = false
                console.log(error)
            })
        },
        searchUsers() {
            this.currentPage = 1
            this.getUsers()
        },
        resetSearch() {
            this.searchQuery = ''
            this.currentPage = 1
            this.getUsers()
        },
        handleCurrentChange(val) {
            this.currentPage = val
            this.getUsers()
        },
        viewUser(user) {
            this.$axios.get("/user/userInfo", {
                params: {
                    id: user.userId
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.userForm = res.data.data
                    this.infoVisible = true
                }
            }).catch(error => {
                console.log(error)
            })
        },
        deleteUser(id) {
            this.$confirm('确定要删除该用户吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$axios.get("/user/delUser", {
                    params:{
                        id: id
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message({
                            message: "删除成功",
                            type: "success"
                        })
                        this.getUsers()
                    } else {
                        this.$message({
                            message: res.data.msg || "删除失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    console.log(error)
                })
            }).catch(() => { })
        }
    },
    mounted() {
        this.getUsers()
    }
}
</script>

<style scoped>
.user-management-container {
    padding: 20px;
}

.search-bar {
    margin-bottom: 20px;
    display: flex;
    gap: 10px;
}

.search-input {
    width: 300px;
}
</style>
