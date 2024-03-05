<template>
  <div class="container">
    <!-- Chart 1 -->
    <div :style="{ height: height, width: width }" :id="id" class="chart"></div>

    <!-- Chart 2 -->
    <div :style="{ height: height, width: width }" id="bar" class="chart"></div>

    <!-- Top 3 Chart -->
    <!--    <div class="top3-chart">-->
    <!--      <div class="chart-title">排行榜</div>-->
    <!--      <div id="top3" class="chart"></div>-->
    <!--    </div>-->
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "echarts",
  props: {
    height: {
      type: String,
      default: "400px",
    },
    width: {
      type: String,
      default: "500px",
    },
    id: {
      type: String,
      default: "demo",
    },
    top3: {
      type: String,
      default: "demo2",
    }
  },
  data() {
    return {
      xValue: [1, 1, 1, 2, 3, 3],
      yValue: ['陕西移动', '山西移动', '北京移动', '山东移动', '河北移动', '河南移动'],
      firstx: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"],
      firsty: [5, 20, 36, 10, 10, 20],
    };
  },
  mounted() {
    this.drawLine();
    this.show();
  },
  methods: {
    drawLine() {
      let myChart = echarts.init(document.getElementById(this.id));
      myChart.setOption({
        title: {text: "费用数据分析"},
        tooltip: {},
        xAxis: {
          type: 'category',
          data: this.firstx
        },
        yAxis: {},
        series: [
          {
            name: "销量",
            type: "bar",
            data: this.firsty
          },
        ],
      });
    },
    show() {
      this.charts = echarts.init(document.getElementById('bar'))
      var option = {
        color: ['#d84430'],
        tooltip: {
          show: true
        },
        yAxis: {
          axisTick: {
            show: true
          },
          axisLine: {
            show: true,
          },
          axisLabel: {
            inside: true,
            verticalAlign: 'bottom',
            lineHeight: 40,
            color: 'rgba(0,0,0,0.87)',
            formatter: function (value, index) {
              if (index > 2) {
                return '{first|' + value + '}'
              } else {
                return '{other|' + value + '}'
              }
            },
            rich: {
              other: {
                color: 'rgba(0,0,0,0.87)',
                opacity: 0.57
              },
              first: {
                color: 'rgba(0,0,0,0.87)'
              }
            }
          },
          data: this.yValue
        },
        xAxis: {
          nameTextStyle: {
            color: 'rgba(255, 255, 255, 0.8)',
            align: 'right'
          },
          splitLine: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: 'rgba(255, 255, 255, 0.8)'
          },
        },
        grid: {
          top: '0%',
          bottom: '0%',
          left: '0%',
          right: '0%'
        },
        series: [{
          barWidth: 15,
          type: 'bar',
          data: this.xValue,
          itemStyle: {
            normal: {
              borderRadius: [3, 20, 20, 3],
              color: function (params) {
                if (params.dataIndex === 5) {
                  return '#d84430'
                } else if (params.dataIndex === 4) {
                  return '#f38237'
                } else if (params.dataIndex === 3) {
                  return '#e2aa20'
                } else {
                  return '#608289'
                }
              }
            },
          }
        }]
      };
      this.charts.setOption(option);
      window.addEventListener('resize', () => {
        this.charts.resize()
      })
    }
  }
};
</script>

<style>
.container {
  display: flex;
  justify-content: space-between; /* Distribute items evenly along the main axis */
}

.chart {
  flex: 1; /* Occupy equal space within the container */
  margin-right: 20px; /* Add spacing between charts */
}

.top3-chart {
  flex: 1; /* Occupy equal space within the container */
  display: flex;
  flex-direction: column; /* Align children vertically */
}

.chart-header {
  display: flex;
  justify-content: center; /* Center-align items horizontally */
  align-items: center; /* Center-align items vertically */
  margin-bottom: 10px; /* Add spacing between title and chart */
}

.chart-title {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.8);
}
</style>

