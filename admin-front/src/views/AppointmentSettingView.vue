<template>
    <div class="appointment-setting-container">
        <el-card>
            <div slot="header">
                <span>预约时间设置</span>
            </div>

            <div class="setting-content">
                <h3>时间设置</h3>
                <el-form :model="settingForm" label-width="100px">
                    <el-form-item label="工作日">
                        <el-checkbox-group v-model="settingForm.workDays">
                            <el-checkbox label="1">周一</el-checkbox>
                            <el-checkbox label="2">周二</el-checkbox>
                            <el-checkbox label="3">周三</el-checkbox>
                            <el-checkbox label="4">周四</el-checkbox>
                            <el-checkbox label="5">周五</el-checkbox>
                            <el-checkbox label="6">周六</el-checkbox>
                            <el-checkbox label="7">周日</el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>

                    <el-form-item label="工作时段">
                        <div class="time-slots">
                            <div v-for="(slot, index) in settingForm.timeSlots" :key="index" class="time-slot-item">
                                <el-time-select v-model="slot.start" placeholder="开始时间" start="08:00" step="00:30"
                                    end="20:00" style="width: 120px; margin-right: 10px;"></el-time-select>
                                <el-time-select v-model="slot.end" placeholder="结束时间" start="08:30" step="00:30"
                                    end="20:30" style="width: 120px; margin-right: 10px;"></el-time-select>
                                <el-button type="danger" size="small" @click="removeTimeSlot(index)">-</el-button>
                            </div>
                            <el-button type="primary" size="small" @click="addTimeSlot">+ 添加时段</el-button>
                        </div>
                    </el-form-item>

                    <el-form-item label="选定日期">
                        <el-date-picker v-model="dateRange" type="daterange" range-separator="至"
                            start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
                        <el-button type="primary" size="small" @click="resetSearch">重置</el-button>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="saveSettings">保存设置</el-button>
                    </el-form-item>
                </el-form>

                <h3 style="margin-top: 30px;">已设置的时间</h3>
                <el-table :data="appointmentList" style="width: 100%" v-loading="loading">
                    <el-table-column label="日期" align="center">
                        <template slot-scope="scope">
                            {{ scope.row.beginTime + " - " + scope.row.endTime }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="200">
                        <template slot-scope="scope">
                            <el-button type="danger" size="small"
                                @click="deleteAppointment(scope.row.schedulingId)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-card>
    </div>
</template>

<script>
export default {
    data() {
        return {
            loading: false,
            settingForm: {
                workDays: ['1', '2', '3', '4', '5'],
                timeSlots: [
                    { start: '09:00', end: '12:00' },
                    { start: '14:00', end: '18:00' }
                ],
            },
            appointmentList: [],
            dateRange: null,
        }
    },
    methods: {
        getAppointments() {
            this.loading = true
            this.$axios.get("/consultation/selectTime", {
                params: {
                    adminId: this.$store.state.adminId
                }
            }).then(res => {
                console.log(res)
                this.loading = false
                if (res.data.code == 1) {
                    this.appointmentList = res.data.data.schedulingList || []
                }
            }).catch(error => {
                this.loading = false
                console.log(error)
            })
        },
        addTimeSlot() {
            this.settingForm.timeSlots.push({ start: '', end: '' })
        },
        removeTimeSlot(index) {
            if (this.settingForm.timeSlots.length > 1) {
                this.settingForm.timeSlots.splice(index, 1)
            } else {
                this.$message({
                    message: "至少需要一个时段",
                    type: "warning"
                })
            }
        },
        saveSettings() {
            const params = {
                adminId: this.$store.state.adminId,
            }
            if (this.dateRange && this.dateRange.length === 2) {
                params.beginTime = this.formatDate(this.dateRange[0])
                params.endTime = this.formatDate(this.dateRange[1])
            }
            this.$axios.get("/consultant/setTime", { params }).then(res => {
                if (res.data.code == 1) {
                    this.$message({
                        message: "设置成功",
                        type: "success"
                    })
                    this.getAppointments()
                } else {
                    this.$message({
                        message: "设置失败",
                        type: "error"
                    })
                }
            }).catch(error => {
                console.log(error)
            })
        },
        //     if (this.settingForm.workDays.length === 0) {
        //         this.$message({
        //             message: "请选择工作日",
        //             type: "warning"
        //         })
        //         return
        //     }
        //     if (this.settingForm.timeSlots.some(slot => !slot.start || !slot.end)) {
        //         this.$message({
        //             message: "请填写所有时段",
        //             type: "warning"
        //         })
        //         return
        //     }
        //     this.$axios.post("/consultant/addTimeSlot", {
        //         consultantId: this.$store.state.adminId,
        //         workDays: this.settingForm.workDays,
        //         timeSlots: this.settingForm.timeSlots,
        //     }).then(res => {
        //         if (res.data.code == 1) {
        //             this.$message({
        //                 message: "设置成功",
        //                 type: "success"
        //             })
        //             this.getAppointments()
        //         } else {
        //             this.$message({
        //                 message: res.data.msg || "设置失败",
        //                 type: "error"
        //             })
        //         }
        //     }).catch(error => {
        //         console.log(error)
        //     })
        // },
        deleteAppointment(id) {
            this.$message({
                message: "日期已被预约不可删除",
                type: "warning"
            })
        },
        resetSearch() {
            this.dateRange = null
        },
        formatDate(date) {
            const d = new Date(date)
            return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
        },
    },
    mounted() {
        if (this.$store.state.adminId !== -1 && this.$store.state.roleId !== -1) {
            this.getAppointments()
        }
    },
    watch: {
        '$store.state.adminId'(newUserId) {
            if (newUserId !== -1) {
                this.getAppointments()
            }
        }
    }
}
</script>

<style scoped>
.appointment-setting-container {
    padding: 20px;
}

.setting-content h3 {
    color: #333;
    margin: 20px 0 15px 0;
}

.time-slots {
    margin-top: 10px;
}

.time-slot-item {
    margin-bottom: 10px;
    display: flex;
    align-items: center;
}
</style>
