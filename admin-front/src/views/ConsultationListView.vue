<template>
    <div class="consultation-list-container">
        <el-card>
            <div slot="header">
                <span>咨询查询列表</span>
            </div>

            <div class="search-bar">
                <el-input placeholder="搜索用户名" v-model="searchQuery.userName" class="search-input" style="width: 200px;"
                    clearable>
                    <el-button slot="append" icon="el-icon-search" @click="searchConsultations"></el-button>
                </el-input>
                <el-input placeholder="搜索咨询师" v-model="searchQuery.consultantName" class="search-input"
                    style="width: 200px;" clearable>
                    <el-button slot="append" icon="el-icon-search" @click="searchConsultations"></el-button>
                </el-input>
                <el-select v-model="status" placeholder="状态" clearable @change="searchConsultations"
                    style="width: 150px;">
                    <el-option label="待审核" value="0"></el-option>
                    <el-option label="已通过" value="1"></el-option>
                    <el-option label="已完成" value="2"></el-option>
                    <el-option label="已取消" value="3"></el-option>
                </el-select>
                <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期"
                    end-placeholder="结束日期" @change="searchConsultations"></el-date-picker>
                <el-button type="primary" size="small" @click="resetSearch">重置</el-button>
            </div>

            <el-table :data="consultationList" style="width: 100%" v-loading="loading">
                <el-table-column prop="reservationId" label="预约ID" width="80"></el-table-column>
                <el-table-column prop="userName" label="用户姓名"></el-table-column>
                <el-table-column prop="consultantName" label="咨询师"></el-table-column>
                <el-table-column prop="orderTime" label="预约日期" width="200"></el-table-column>
                <el-table-column prop="status" label="状态">
                    <template slot-scope="scope">
                        <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="创建时间" width="200">
                    <template slot-scope="scope">
                        {{ scope.row.createTime | formatTime }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="250">
                    <template slot-scope="scope">
                        <el-button type="primary" size="small" @click="viewConsultation(scope.row)"
                            style="margin-right: 10px;">查看</el-button>
                        <span v-if="$store.state.roleId !== 1 && $store.state.roleId !== 2">
                            <el-button v-if="scope.row.status === 0" type="success" size="small"
                                @click="reviewConsultation(scope.row.reservationId)">审核</el-button>
                            <el-button v-if="scope.row.status === 1" type="info" size="small"
                                @click="diagnosisConsultation(scope.row.reservationId)">诊断</el-button>
                            <el-button type="danger" size="small"
                                @click="cancelConsultation(scope.row.reservationId)">删除</el-button>
                        </span>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
                layout="prev, pager, next, total" :total="total">
            </el-pagination>
        </el-card>

        <!-- 查看预约对话框 -->
        <el-dialog title="预约详情" :visible.sync="detailDialogVisible" width="600px">
            <div v-if="currentConsultation.reservationId" class="consultation-detail">
                <h4>基本信息</h4>
                <p><strong>用户：</strong>{{ currentConsultation.userName }}</p>
                <p><strong>咨询师：</strong>{{ currentConsultation.consultantName }}</p>
                <p><strong>预约日期：</strong>{{ currentConsultation.orderTime }}</p>
                <p><strong>状态：</strong><el-tag :type="getStatusType(currentConsultation.status)">{{
                    getStatusText(currentConsultation.status) }}</el-tag></p>
                <el-divider></el-divider>
                <h4>问题描述</h4>
                <p>{{ currentConsultation.problem || '暂无描述' }}</p>
                <el-divider v-if="currentConsultation.answer"></el-divider>
                <h4 v-if="currentConsultation.answer">咨询报告</h4>
                <p v-if="currentConsultation.answer">{{ currentConsultation.answer }}</p>
                <el-divider v-if="currentConsultation.rate"></el-divider>
                <h4 v-if="currentConsultation.rate">用户评价</h4>
                <p v-if="currentConsultation.rate"><strong>评价内容：</strong>{{ currentConsultation.rate }}</p>
                <p v-if="currentConsultation.rateTime"><strong>评价日期：</strong>{{ currentConsultation.rateTime }}</p>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            consultationList: [],
            loading: false,
            currentPage: 1,
            pageSize: 10,
            total: 0,
            searchQuery: {
                userName: null,
                consultantName: null
            },
            status: null,
            dateRange: null,
            detailDialogVisible: false,
            currentConsultation: {}
        }
    },
    methods: {
        getConsultations() {
            this.loading = true
            const params = {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                status: this.status
            }
            if (this.searchQuery) {
                params.userName = this.searchQuery.userName
                params.consultantName = this.searchQuery.consultantName
            }
            if (this.dateRange && this.dateRange.length === 2) {
                params.startDate = this.dateRange[0]
                params.endDate = this.dateRange[1]
            }
            if (this.$store.state.roleId != 1 && this.$store.state.roleId != 2) {
                params.adminId = this.$store.state.adminId
            }
            this.$axios.get("/consultation/selectReservation", { params }).then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.consultationList = res.data.data.list || []
                    this.total = res.data.data.total || 0
                }
            }).catch(error => {
                this.loading = false
                console.log(error)
            })
        },
        searchConsultations() {
            this.currentPage = 1
            this.getConsultations()
        },
        resetSearch() {
            this.searchQuery = ''
            this.status = null
            this.dateRange = null
            this.currentPage = 1
            this.getConsultations()
        },
        handleCurrentChange(val) {
            this.currentPage = val
            this.getConsultations()
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
        viewConsultation(consultation) {
            this.$axios.get("/consultation/selectReservationById", {
                params: {
                    reservationId: consultation.reservationId
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.currentConsultation = res.data.data
                    this.detailDialogVisible = true
                }
            }).catch(error => {
                console.log(error)
            })
        },
        reviewConsultation(id) {
            this.$confirm('确定要通过该预约吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'success'
            }).then(() => {
                this.$axios.get("/consultant/reviewReservation", {
                    params: {
                        id: id
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message({
                            message: "审核通过",
                            type: "success"
                        })
                        this.getConsultations()
                    } else {
                        this.$message({
                            message: res.data.msg || "审核失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    console.log(error)
                })
            }).catch(() => { })
        },
        diagnosisConsultation(id) {
            this.$router.push(`/main/consultation-diagnosis?id=${id}`)
        },
        cancelConsultation(id) {
            this.$confirm('确定要取消该预约吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$axios.get("/consultation/delReservation", {
                    params: {
                        reservationId: id
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message({
                            message: "取消成功",
                            type: "success"
                        })
                        this.getConsultations()
                    } else {
                        this.$message({
                            message: res.data.msg || "取消失败",
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
        if (this.$store.state.adminId !== -1 && this.$store.state.roleId !== -1)
            this.getConsultations()
    },
    watch: {
        '$store.state.adminId'(newUserId) {
            if (newUserId !== -1) {
                this.getConsultations()
            }
        }
    }
}
</script>

<style scoped>
.consultation-list-container {
    padding: 20px;
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

.consultation-detail h4 {
    color: #333;
    margin: 15px 0 10px 0;
}

.consultation-detail p {
    margin: 5px 0;
    color: #666;
}
</style>
