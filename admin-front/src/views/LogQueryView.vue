<template>
    <div class="log-query-container">
        <el-card>
            <div slot="header">
                <span>日志查询</span>
            </div>

            <div class="search-bar">
                <el-select v-model="type" placeholder="选择事件" clearable @change="getLogs">
                    <el-option label="新增用户" value="1"></el-option>
                    <el-option label="新增医师" value="2"></el-option>
                    <el-option label="新增咨询" value="3"></el-option>
                    <el-option label="新增答题" value="4"></el-option>
                </el-select>
                <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期"
                    end-placeholder="结束日期" @change="getLogs"></el-date-picker>
                <el-button type="primary" size="small" @click="resetSearch">重置</el-button>
            </div>

            <el-table :data="logList" style="width: 100%" v-loading="loading">
                <el-table-column prop="logId" label="日志ID" width="200"></el-table-column>
                <el-table-column label="内容">
                    <template slot-scope="scope">
                        {{ showType(scope.row.type) }}
                    </template>
                </el-table-column>
                <el-table-column label="操作时间">
                    <template slot-scope="scope">
                        {{ scope.row.createTime | formatTime }}
                    </template>
                </el-table-column>
            </el-table>

        </el-card>
    </div>
</template>

<script>
export default {
    data() {
        return {
            logList: [],
            loading: false,
            type: null,
            dateRange: [],
            startDate: null,
            endDate: null
        }
    },
    methods: {
        getLogs() {
            this.loading = true
            if (this.dateRange && this.dateRange.length === 2) {
                this.startDate = this.formatDate(this.dateRange[0])
                this.endDate = this.formatDate(this.dateRange[1])
            } else {
                this.startDate = null;
                this.endDate = null;
            }
            this.$axios.get("/system/selectLog", {
                params: {
                    startDate: this.startDate,
                    endDate: this.endDate,
                    type: this.type
                }
            }).then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.logList = res.data.data || []
                }
            }).catch(error => {
                this.loading = false
                console.log(error)
            })
            this.type = null
        },
        resetSearch() {
            this.dateRange = null
            this.getLogs()
        },
        showType(id) {
            if (id === 1) return '新增用户'
            if (id === 2) return '新增医师'
            if (id === 3) return '新增咨询'
            if (id === 4) return '新增答题'
            return '未知类型'
        },
        formatDate(date) {
            const d = new Date(date)
            return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
        },
    },

    mounted() {
        this.getLogs()
    },
}
</script>

<style scoped>
.log-query-container {
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
</style>
