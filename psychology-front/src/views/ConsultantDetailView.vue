<template>
    <div class="consultant-detail-container">
        <el-card class="consultant-profile">
            <div class="profile-header">
                <el-avatar :size="120" :src="consultant.headPicture || defaultAvatar"></el-avatar>
                <div class="profile-info">
                    <h2>{{ consultant.name }}</h2>
                    <p class="domain">{{ consultant.domainName }}</p>
                    <el-rate v-model="consultant.rateScore" disabled show-score text-color="#ff9900"></el-rate>
                    <div class="stats">
                        <span><i class="el-icon-s-custom"></i> {{ consultant.role }} </span>
                        <span><i class="el-icon-money"></i> ¥{{ consultant.price }}/时段</span>
                    </div>
                </div>
                <div class="profile-actions">
                    <el-button type="primary" size="large" @click="makeAppointment">立即预约</el-button>
                    <el-button type="warning" size="large" @click="Backtop">返回</el-button>
                </div>
            </div>

            <el-divider></el-divider>
            <div class="profile-section">
                <h3>个人简介</h3>
                <p>{{ consultant.profile || '暂无简介' }}</p>
            </div>
            <div class="profile-section">
                <h3>擅长问题</h3>
                <p v-if="consultant.domain && consultant.domain.length > 0">
                    <span v-for="(item, index) in consultant.domain" :key="index">
                        {{ item.name }}
                        <span v-if="index < consultant.domain.length - 1">、</span> <!-- 加顿号分隔 -->
                    </span>
                </p>
                <p v-else>{{ '暂无信息' }}</p>
            </div>
            <div class="profile-section">
                <h3>毕业学校</h3>
                <p>{{ consultant.school || '暂无学校信息' }}</p>
            </div>
            <div class="profile-section">
                <h3>专业资质</h3>
                <p>{{ consultant.project || '暂无资质信息' }}</p>
            </div>

        </el-card>

        <el-card class="reviews-section">
            <div slot="header">
                <span>用户评价</span>
            </div>
            <div v-if="reviewList.length === 0" class="no-reviews">
                <span>暂无评价</span>
            </div>
            <div v-else>
                <div v-for="review in reviewList" :key="review.reservationId" class="review-item">
                    <div class="review-list">
                        <div class="review-header">
                            <span class="reviewer">{{ review.userName }}</span>
                            <el-rate v-model="review.rateScore" disabled></el-rate>
                            <span class="review-time">{{ review.rateTime }}</span>
                        </div>
                        <p class="review-content">{{ review.rate }}</p>
                    </div>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>

export default {
    data() {
        return {
            consultantId: null,
            consultant: {},
            reviewList: [],
            defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
        }
    },
    methods: {
        getConsultantDetail() {
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
        getReviews() {
            this.$axios.get("/consultation/selectReservation", {
                params: {
                    consultantId: this.consultantId,
                    status: 2
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.reviewList = (res.data.data.list || [])
                        .filter(r => r.rate != null && r.rate != '')
                }
            }).catch(error => {
                console.log(error)
            })
        },
        makeAppointment() {
            this.$router.push(`/appointment/${this.consultantId}`)
        },
        Backtop() {
            this.$router.push(`/consultant-list`)
        }
    },
    mounted() {
        this.consultantId = this.$route.params.id
        this.getConsultantDetail()
        this.getReviews()
    }
}
</script>

<style scoped>
.consultant-detail-container {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
}

.consultant-profile {
    margin-bottom: 20px;
}

.profile-header {
    display: flex;
    align-items: center;
    gap: 30px;
}

.profile-info {
    flex: 1;
}

.profile-info h2 {
    margin: 0 0 10px 0;
}

.domain {
    color: #666;
    margin: 5px 0;
}

.stats {
    margin-top: 10px;
}

.stats span {
    margin-right: 20px;
    color: #666;
}

.profile-section {
    margin: 20px 0;
}

.profile-section h3 {
    color: #333;
    margin-bottom: 10px;
}

.reviews-section {
    margin-top: 20px;
}

.no-reviews {
    text-align: center;
    color: #999;
    padding: 40px;
}

.review-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.review-item {
    border-bottom: 1px solid #eee;
    padding-bottom: 15px;
}

.review-header {
    display: flex;
    align-items: center;
    gap: 15px;
    margin-bottom: 10px;
}

.reviewer {
    font-weight: bold;
}

.review-time {
    color: #999;
    font-size: 12px;
}

.review-content {
    color: #666;
    line-height: 1.6;
}
</style>
