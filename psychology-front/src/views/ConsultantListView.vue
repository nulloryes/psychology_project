<template>
  <div class="consultant-list-container">
    <div class="search-bar">
      <el-input placeholder="搜索咨询师姓名" v-model="searchName" class="search-input" clearable>
        <el-button slot="append" icon="el-icon-search" @click="searchConsultants"></el-button>
      </el-input>
      <el-select v-model="selectedDomain" placeholder="选择问题" clearable @change="searchConsultants">
        <el-option v-for="domain in domainList" :key="domain.domainId" :label="domain.name" :value="domain.domainId"></el-option>
      </el-select>
      <el-select v-model="selectedRole" placeholder="选择职称" clearable @change="searchConsultants">
        <el-option label="全部" value=''></el-option>
        <el-option label="主任医师" value="主任医师"></el-option>
        <el-option label="副主任医师" value="副主任医师"></el-option>
        <el-option label="主治医师" value="主治医师"></el-option>
        <el-option label="医师" value="医师"></el-option>
      </el-select>
    </div>
    
    <div class="consultant-grid">
      <el-card v-for="consultant in consultantList" :key="consultant.consultantId" class="consultant-card" shadow="hover">
        <div class="consultant-header">
          <el-avatar :size="80" :src="consultant.headPicture || defaultAvatar"></el-avatar>
          <div class="consultant-info">
            <h3>{{ consultant.name }}</h3>
            <p class="domain">{{ consultant.project }}</p>
            <el-rate v-model="consultant.rateScore" disabled show-score text-color="#ff9900"></el-rate>
          </div>
        </div>
        <div class="consultant-detail">
          <p><strong>医院职称：</strong>{{ consultant.role }}</p>
          <p><strong>咨询费用：</strong>¥{{ consultant.price }}/时段</p>
          <p><strong>个人简介：</strong>{{ consultant.profile || '暂无简介' }}</p>
        </div>
        <div class="consultant-actions">
          <el-button type="primary" size="small" @click="viewDetail(consultant.consultantId)">查看详情</el-button>
          <el-button type="success" size="small" @click="makeAppointment(consultant.consultantId)">立即预约</el-button>
        </div>
      </el-card>
    </div>
    
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next, total"
      :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
    data(){
        return{
            searchName: null,
            selectedDomain: null,
            selectedRole: null,
            consultantList: [],
            domainList: [],
            currentPage: 1,
            pageSize: 8,
            total: 0,
            defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
        }
    },
    methods:{
        getConsultantList(){
            this.$axios.get("/consultant/allConsultant",{
                params:{
                    name: this.searchName,
                    domainId: this.selectedDomain,
                    role: this.selectedRole,
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            }).then(res=>{
                if(res.data.code == 1){
                    this.consultantList = res.data.data.list
                    this.total = res.data.data.total
                }
            }).catch(error =>{
                console.log(error)
            })
        },
        getDomainList(){
            this.$axios.get("/domain/allDomain").then(res=>{
                if(res.data.code == 1){
                    this.domainList = res.data.data || []
                }
            }).catch(error =>{
                console.log(error)
            })
        },
        searchConsultants(){
            this.currentPage = 1
            this.getConsultantList()
        },
        handleCurrentChange(val){
            this.currentPage = val
            this.getConsultantList()
        },
        viewDetail(id){
            this.$router.push(`/consultant-detail/${id}`)
        },
        makeAppointment(id){
            this.$router.push(`/appointment/${id}`)
        }
    },
    mounted(){
        this.getConsultantList()
        this.getDomainList()
    }
}
</script>

<style scoped>
.consultant-list-container {
    padding: 20px;
}
.search-bar {
    display: flex;
    gap: 20px;
    margin-bottom: 20px;
    flex-wrap: wrap;
}
.search-input {
    width: 300px;
}
.el-select {
    min-width: 150px;
}
.el-input-number {
    min-width: 120px;
}
.consultant-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 20px;
    margin-bottom: 20px;
}
.consultant-card {
    transition: transform 0.3s;
}
.consultant-card:hover {
    transform: translateY(-5px);
}
.consultant-header {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}
.consultant-info {
    margin-left: 15px;
}
.consultant-info h3 {
    margin: 0 0 5px 0;
}
.domain {
    color: #666;
    margin: 5px 0;
}
.consultant-detail {
    margin-bottom: 15px;
}
.consultant-detail p {
    margin: 5px 0;
    color: #666;
}
.consultant-actions {
    display: flex;
    justify-content: space-between;
}
</style>
