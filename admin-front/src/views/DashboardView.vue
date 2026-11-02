<template>
  <div class="dashboard-container">
    <el-card class="stats-card">
      <div slot="header">
        <span>系统统计</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon user-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <h3>{{ stats.newUsers }}</h3>
              <p>当月新增用户</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon consultant-icon">
              <i class="el-icon-s-custom"></i>
            </div>
            <div class="stat-info">
              <h3>{{ stats.newConsultants }}</h3>
              <p>当月新增医师</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon consultation-icon">
              <i class="el-icon-date"></i>
            </div>
            <div class="stat-info">
              <h3>{{ stats.newConsultations }}</h3>
              <p>当月新增咨询</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon answer-icon">
              <i class="el-icon-question"></i>
            </div>
            <div class="stat-info">
              <h3>{{ stats.newAnswers }}</h3>
              <p>当月新增答题</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="chart-card" style="margin-top: 20px;">
      <div slot="header">
        <span>数据趋势</span>
      </div>
      <div class="chart-container">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="用户增长" name="users">
            <div class="chart" ref="userChart"></div>
          </el-tab-pane>
          <el-tab-pane label="咨询趋势" name="consultations">
            <div class="chart" ref="consultationChart"></div>
          </el-tab-pane>
          <el-tab-pane label="答题统计" name="answers">
            <div class="chart" ref="answerChart"></div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  data() {
    return {
      stats: {
        newUsers: 0,
        newConsultants: 0,
        newConsultations: 0,
        newAnswers: 0
      },
      log: [],
      activeTab: 'users',
      charts: {
        users: null,
        consultations: null,
        answers: null
      }
    }
  },
  methods: {
    getStats() {
      this.$axios.get("/system/selectLog")
        .then(res => {
          if (res.data.code == 1) {
            this.log = res.data.data
            this.stats = {
              newUsers: this.countLog(1),
              newConsultants: this.countLog(2),
              newConsultations: this.countLog(3),
              newAnswers: this.countLog(4)
            }
            this.initChart('users')
          } else {
            this.$message({
              message: '加载失败',
              type: "error"
            });
          }
        })
    },
    initCharts() {
      this.initChart('users')
      this.initChart('consultations')
      this.initChart('answers')
    },
    initChart(type) {
      const chartRefs = {
        users: this.$refs.userChart,
        consultations: this.$refs.consultationChart,
        answers: this.$refs.answerChart
      }
      const chartDom = chartRefs[type]
      if (!chartDom) return
      // 确保DOM元素有宽高
      if (chartDom.clientWidth === 0 || chartDom.clientHeight === 0) {
        setTimeout(() => this.initChart(type), 100)
        return
      }
      if (this.charts[type]) {
        this.charts[type].dispose()
      }
      this.charts[type] = echarts.init(chartDom)
      const chartConfig = this.getChartConfig(type)
      this.charts[type].setOption(chartConfig)
    },
    getChartConfig(type) {
      const months = this.getMonthLabels()
      const typeConfig = {
        users: {
          title: '用户增长趋势',
          data: this.getMonthlyData(1),
          unit: '人',
          color: '#667eea',
          name: '新增用户'
        },
        consultations: {
          title: '咨询趋势',
          data: this.getMonthlyData(3),
          unit: '次',
          color: '#4facfe',
          name: '新增咨询'
        },
        answers: {
          title: '答题统计',
          data: this.getMonthlyData(4),
          unit: '题',
          color: '#43e97b',
          name: '新增答题'
        }
      }

      const config = typeConfig[type]

      return {
        title: {
          text: config.title,
          left: 'center',
          textStyle: {
            color: '#333',
            fontSize: 16
          }
        },
        tooltip: {
          trigger: 'axis',
          formatter: `{b}<br/>{a}: {c}${config.unit}`
        },
        xAxis: {
          type: 'category',
          data: months,
          boundaryGap: false
        },
        yAxis: {
          type: 'value',
          name: config.unit,
          axisLabel: {
            formatter: '{value}'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        series: [
          {
            name: config.name,
            type: 'line',
            data: config.data,
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            itemStyle: {
              color: config.color
            },
            lineStyle: {
              width: 3,
              color: config.color
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: `${config.color}33` },
                { offset: 1, color: `${config.color}05` }
              ])
            }
          }
        ]
      }
    },
    getMonthLabels() {
      const now = new Date()
      const months = []
      for (let i = 5; i >= 0; i--) {
        const date = new Date(now.getFullYear(), now.getMonth() - i, 1)
        months.push(`${date.getMonth() + 1}月`)
      }
      return months
    },
    getMonthlyData(type) {
      if (!this.log || this.log.length === 0) {
        return [0, 0, 0, 0, 0, 0]
      }
      const now = new Date()
      const monthlyData = [0, 0, 0, 0, 0, 0]
      this.log.forEach(item => {
        if (item.type == type) {
          const itemDate = new Date(item.createTime)
          const monthDiff = (now.getFullYear() - itemDate.getFullYear()) * 12 + (now.getMonth() - itemDate.getMonth())
          if (monthDiff >= 0 && monthDiff < 6) {
            monthlyData[5 - monthDiff]++
          }
        }
      })
      return monthlyData
    },
    countLog(type) {
      if (!this.log) return 0;
      let count = 0;
      const now = new Date();
      this.log.forEach(item => {
        const data = new Date(item.createTime)
        if (item.type == type && data.getMonth() == now.getMonth() && data.getFullYear() == now.getFullYear()) {
          count++;
        }
      });
      return count;
    },
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        if (chart) chart.resize()
      })
    },
    handleTabClick() {
      // 切换标签时确保图表正确渲染
      this.$nextTick(() => {
        this.initChart(this.activeTab)
      })
    }
  },
  mounted() {
    this.getStats()
    this.$nextTick(() => {
      this.initCharts()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(chart => {
      if (chart) chart.dispose()
    })
    window.removeEventListener('resize', this.handleResize)
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.stats-card {
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  transition: transform 0.3s;
}

.stat-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.consultant-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.consultation-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.answer-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info h3 {
  margin: 0 0 5px 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-info p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.chart-card {
  height: 550px;
}

.chart-container {
  height: 400px;
}

.chart {
  height: 100%;
  background: #f5f7fa;
  border-radius: 8px;
  min-width: 300px;
  min-height: 300px;
}
</style>
