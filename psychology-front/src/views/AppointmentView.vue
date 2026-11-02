<template>
    <div class="appointment-container">
        <el-card class="appointment-card">
            <div slot="header">
                <span>预约咨询师</span>
            </div>

            <div class="consultant-info" v-if="consultant.consultantId">
                <el-avatar :size="60" :src="consultant.headPicture || defaultAvatar"></el-avatar>
                <div class="info-text">
                    <h3>{{ consultant.name }}</h3>
                    <p>{{ consultant.domainName }} | ¥{{ consultant.price }}/天</p>
                </div>
            </div>

            <div class="consultant-info" v-if="!consultant.consultantId">
                <h2>你还没有选择咨询师（*。*）</h2>
                <el-button type="primary" @click="goBack">立即预约</el-button>
            </div>

            <el-divider></el-divider>

            <div class="appointment-form">
                <h4>选择预约时间（必填）：</h4>
                <el-calendar v-model="selectedDate"></el-calendar>

                <div class="time-slots" v-if="isAppointment === 1">
                    <h4 style="color: green;">此日可预约</h4>
                    <div class="time-slot-buttons">
                        <el-button v-for="slot in timeSlots" :key="slot.value"
                            :type="selectedTimeSlot === slot.value ? 'primary' : 'default'" :disabled="slot.disabled"
                            @click="selectTimeSlot(slot.value)" class="time-slot-btn">
                            {{ slot.label }}
                        </el-button>
                    </div>
                    <p v-if="selectedTimeSlot" class="selected-time-info">
                        已选择时间段: {{ getTimeSlotLabel(selectedTimeSlot) }}
                    </p>
                </div>
                <div class="time-slots" v-else-if="isAppointment === 2">
                    <h4 style="color: red;">该日已被预约</h4>
                </div>
                <div v-else-if="selectedDate" class="no-slots">
                    咨询师无排班
                </div>

                <h4>主要问题（必填）：</h4>
                <el-select v-model="selectedDomain" placeholder="选择问题" clearable>
                    <el-option v-for="domain in domainList" :key="domain.domainId" :label="domain.name"
                        :value="domain.domainId"></el-option>
                </el-select>

                <div class="problem-description">
                    <h4>问题描述：</h4>
                    <el-input type="textarea" :rows="4" placeholder="请简要描述您想咨询的问题（可选）" v-model="problemDesc">
                    </el-input>
                </div>

                <div class="form-actions">
                    <el-button @click="goBack">返回</el-button>
                    <el-button type="primary" @click="submitAppointment"
                        :disabled="!selectedDate || !selectedDomain || !selectedTimeSlot">确认预约</el-button>
                </div>
            </div>
        </el-card>

        <el-card class="my-appointments">
            <div slot="header">
                <span>我的预约记录</span>
            </div>
            <el-table :data="appointmentList" style="width: 100%">
                <el-table-column prop="consultantName" label="咨询师"></el-table-column>
                <el-table-column prop="orderTime" label="预约日期"></el-table-column>
                <el-table-column prop="problem" label="你的问题"></el-table-column>
                <el-table-column prop="status" label="状态">
                    <template slot-scope="scope">
                        <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="你的评价">
                    <template slot-scope="scope">
                        <span v-if="scope.row.rate !== null && scope.row.rate !== ''">
                            {{ scope.row.rate }}
                        </span>
                        <span v-else style="color: #999;">暂无评价</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button v-if="scope.row.status === 0" type="danger" size="mini"
                            @click="cancelAppointment(scope.row.reservationId)">取消</el-button>
                        <el-button v-if="scope.row.status === 2 && (scope.row.rate == null || scope.row.rate == '')" 
                        type="primary" size="mini" @click="goToReview(scope.row.reservationId)">评价</el-button>
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
            consultantId: null,
            consultant: {},
            selectedDate: null,
            // selectedSlot: null,
            availableSlots: {},
            selectedDomain: null,
            domainList: [],
            appointed: [],
            schedulingList: [], // 排班列表
            problemDesc: null,
            appointmentList: [],
            defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
            // 时间段选择相关
            timeSlots: [
                { label: '8:00-10:00', value: '08:00:00', disabled: false },
                { label: '10:00-12:00', value: '10:00:00', disabled: false },
                { label: '14:00-16:00', value: '14:00:00', disabled: false },
                { label: '16:00-18:00', value: '16:00:00', disabled: false }
            ],
            selectedTimeSlot: null
        }
    },
    methods: {
        getConsultantInfo() {
            this.$axios.get("/consultant/consultantInfo", {
                params: {
                    id: this.consultantId
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.consultant = res.data.data || {}
                }
            }).catch(error => {
                console.log(error)
            })
        },
        getAvailableSlots() {
            this.$axios.get("/consultation/selectTime", {
                params: {
                    consultantId: this.consultantId,
                }
            }).then(res => {
                if (res.data.code == 1) {
                    console.log('排班数据:', res)
                    this.availableSlots = res.data.data || {}
                    this.appointed = res.data.data.reservedList || []
                    // 解析新的排班列表数据
                    this.schedulingList = res.data.data.schedulingList || []
                }
            }).catch(error => {
                console.log(error)
            })
        },
        getDomainList() {
            this.$axios.get("/domain/allDomain").then(res => {
                if (res.data.code == 1) {
                    this.domainList = res.data.data || []
                }
            }).catch(error => {
                console.log(error)
            })
        },
        submitAppointment() {
            if (!this.selectedTimeSlot) {
                this.$message({
                    message: "请选择预约时间段",
                    type: "warning"
                })
                return
            }
            // 构建完整的预约时间，包含日期和时间段
            const dateStr = this.formatDate(this.selectedDate)
            const orderTimeStr = `${dateStr} ${this.selectedTimeSlot}`
            this.$axios.get("/consultation/insertReservation", {
                params: {
                    userId: this.$store.state.userId,
                    consultantId: this.consultantId,
                    domainId: this.selectedDomain,
                    orderTime: orderTimeStr,
                    problem: this.problemDesc,
                    money: this.consultant.price
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message({
                        message: "预约成功",
                        type: "success"
                    })
                    this.getMyAppointments()
                    this.selectedTimeSlot = null
                    this.problemDesc = null
                } else {
                    this.$message({
                        message: res.data.msg || "预约失败",
                        type: "error"
                    })
                }
            }).catch(error => {
                console.log(error)
            })
        },
        getMyAppointments() {
            this.$axios.get("/consultation/selectReservation", {
                params: {
                    userId: this.$store.state.userId
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.appointmentList = res.data.data.list || []
                }
            }).catch(error => {
                console.log(error)
            })
        },
        cancelAppointment(id) {
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
                        this.getMyAppointments()
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
        },
        goToReview(id) {
            this.$router.push(`/review/${id}`)
        },
        // 选择时间段
        selectTimeSlot(timeValue) {
            if (this.isTimeSlotDisabled(timeValue)) {
                this.$message({
                    message: '该时间段已被预约',
                    type: 'warning'
                })
                return
            }
            this.selectedTimeSlot = timeValue
            this.$message({
                message: `已选择 ${this.getTimeSlotLabel(timeValue)}`,
                type: 'success'
            })
        },
        // 获取时间段标签
        getTimeSlotLabel(timeValue) {
            const slot = this.timeSlots.find(s => s.value === timeValue)
            return slot ? slot.label : timeValue
        },
        // 检查时间段是否已被预约或不在排班范围内
        isTimeSlotDisabled(timeValue) {
            if (!this.selectedDate) {
                return true
            }
            const dateStr = this.formatDate(this.selectedDate)
            // 首先检查是否在排班范围内
            const inSchedule = this.isTimeSlotInSchedule(dateStr, timeValue)
            if (!inSchedule) {
                return true // 不在排班范围内，禁用
            }
            // 然后检查是否已被预约
            if (this.appointed && this.appointed.length > 0) {
                const isReserved = this.appointed.some(item => {
                    if (!item || !item.orderTime) return false
                    const orderTime = item.orderTime
                    // 匹配日期和时间段
                    console.log(item)
                    console.log(orderTime)
                     console.log(dateStr)   
                    return orderTime.startsWith(dateStr) && orderTime.includes(timeValue)
                })
                if (isReserved) {
                    return true // 已被预约，禁用
                }
            }
            return false // 可用
        },
        // 更新时间段按钮状态
        updateTimeSlotsStatus() {
            this.timeSlots = this.timeSlots.map(slot => ({
                ...slot,
                disabled: this.isTimeSlotDisabled(slot.value)
            }))
        },
        // 检查指定日期和时间段是否在排班范围内
        isTimeSlotInSchedule(dateStr, timeValue) {
            if (!this.schedulingList || this.schedulingList.length === 0) {
                return false
            }
            const dateObj = new Date(dateStr)
            // 构建完整的时间字符串进行比较
            const dateTimeStr = `${dateStr} ${timeValue}`
            return this.schedulingList.some(schedule => {
                if (!schedule || !schedule.beginTime || !schedule.endTime) return false
                // 检查日期是否在排班范围内
                const beginDate = new Date(schedule.beginTime)
                const endDate = new Date(schedule.endTime)
                if (dateObj < beginDate || dateObj > endDate) {
                    return false
                }
                // 如果排班有具体的时间段限制，检查时间段是否匹配
                if (schedule.beginTime && schedule.endTime) {
                    const scheduleBeginTime = schedule.beginTime.split(' ')[1] || ''
                    const scheduleEndTime = schedule.endTime.split(' ')[1] || ''
                    // 如果排班有具体时间段，检查是否在范围内
                    if (scheduleBeginTime && scheduleEndTime) {
                        return timeValue >= scheduleBeginTime && timeValue <= scheduleEndTime
                    }
                }
                return true
            })
        },
        goBack() {
            this.$router.push("/consultant-list")
        },
        formatDate(date) {
            const d = new Date(date)
            return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
        },
        disabledDate(date) {
            const today = new Date()
            const compareDate = new Date(date)
            return compareDate < today
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
        }
    },
    mounted() {
        this.consultantId = this.$route.params.id
        this.getDomainList()
        if (this.consultantId) {
            this.getConsultantInfo()
            this.getAvailableSlots()
        }
        if (this.$store.state.userId !== -1) {
            this.getMyAppointments()
        }
    },
    computed: {
        isAppointment() {
            if (!this.selectedDate) return 0;
            const selectedDateStr = this.formatDate(this.selectedDate);
            const selectedDateObj = new Date(selectedDateStr);
            // 检查是否在排班列表中
            if (this.schedulingList && this.schedulingList.length > 0) {
                const isInSchedule = this.schedulingList.some(schedule => {
                    if (!schedule || !schedule.beginTime || !schedule.endTime) return false;
                    const beginDate = new Date(schedule.beginTime);
                    const endDate = new Date(schedule.endTime);
                    return selectedDateObj >= beginDate && selectedDateObj <= endDate;
                });
                if (isInSchedule) {
                    return 1;
                }
            }
            return 0;
        }
    },
    watch: {
        '$store.state.userId'(newUserId) {
            if (newUserId !== -1) {
                this.getMyAppointments()
            }
        },
        selectedDate(newDate) {
            this.selectedTimeSlot = null;
            if (!newDate) return;
            // 检查是否选择了过去的日期
            if (this.disabledDate(newDate)) {
                this.$message({
                    message: '不可溯回预约',
                    type: 'warning'
                });
                this.$nextTick(() => {
                    this.selectedDate = null;
                });
                return;
            }
            // 更新时间段按钮状态
            this.$nextTick(() => {
                this.updateTimeSlotsStatus();
            });
        }
    }
}
</script>

<style scoped>
.appointment-container {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
}

.appointment-card {
    margin-bottom: 20px;
}

.consultant-info {
    display: flex;
    align-items: center;
    gap: 15px;
}

.info-text h3 {
    margin: 0 0 5px 0;
}

.info-text p {
    color: #666;
    margin: 0;
}

.appointment-form {
    margin-top: 20px;
}

.time-slots {
    font-size: x-large;
    margin: 20px 0;
}

.no-slots {
    color: #999;
    margin: 20px 0;
}

.problem-description {
    margin: 20px 0;
}

.form-actions {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 30px;
}

.my-appointments {
    margin-top: 20px;
}

.time-slot-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    margin-top: 15px;
    justify-content: center;
}

.time-slot-btn {
    min-width: 120px;
    padding: 12px 20px;
    font-size: 14px;
}

.time-slot-btn.is-disabled {
    background-color: #f5f7fa;
    border-color: #e4e7ed;
    color: #c0c4cc;
    cursor: not-allowed;
}

.selected-time-info {
    margin-top: 15px;
    color: #409EFF;
    font-weight: bold;
    text-align: center;
}
</style>