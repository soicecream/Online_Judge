<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px;">
      <el-col :span="6">
        <el-card style="color: #409EFF;">
          <div>用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;">
            1000
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card style="color: #fa4659;">
          <div>昨日在线人数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;">
            1000
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card style="color: #ff9a00;">
          <div>昨日提交数量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;">
            1000
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card style="color: #62d2a2;">
          <div>总提交数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;">
            1000
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 400px;"/>
      </el-col>

      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 400px;"/>
      </el-col>

    </el-row>

  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "home",

  data() {
    return {}
  },


  mounted() {
    let myChart = echarts.init(document.getElementById('main'));
    let option = {
      title: {
        text: "各级度会员数量",
        subtext: "趋势图",
        left: "center",
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: "提交",
          data: [],
          type: 'line'
        },
        {
          name: "提交",
          data: [],
          type: 'bar'
        },
        {
          name: "AC",
          data: [],
          type: 'line'
        },
        {
          name: "AC",
          data: [],
          type: 'bar'
        }

      ]
    };

    let pieChart = echarts.init(document.getElementById('pie'));
    let pieOption = {
      title: {
        text: "各级度会员数量",
        subtext: "趋势图",
        left: "center",
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: "AC",
          type: 'pie',
          radius: '50%',
          center: ['25%', '50%'],
          // 饼图图形上的文本标签
          label: {
            normal: {
              show: true,
              // 标签位置
              position: 'inner',
              textStyle: {
                fontWeight: 300,
                // 文字的字体大小
                fontSize: 16,
                color: "#fff",
              },
              formatter: "{d}%",
            }
          },
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        },
        {
          name: "AC",
          type: 'pie',
          radius: '50%',
          center: ['75%', '50%'],
          // 饼图图形上的文本标签
          label: {
            normal: {
              show: true,
              // 标签位置
              position: 'inner',
              textStyle: {
                fontWeight: 300,
                // 文字的字体大小
                fontSize: 16,
                color: "#fff",
              },
              formatter: "{d}%",
            }
          },
          data: [
            {name: "第一季度", value: 5},
            {name: "第二季度", value: 6},
            {name: "第三季度", value: 7},
            {name: "第四季度", value: 8},
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }

      ]
    }

    this.request.get("/echarts/members").then(res => {
      if (res.code === '200') {
        // option.xAxis.data = res.data.x
        option.series[0].data = res.data
        option.series[1].data = res.data

        option.series[2].data = [5, 6, 7, 8]
        option.series[3].data = [5, 6, 7, 8]
        myChart.setOption(option)

        pieOption.series[0].data = [
          {name: "第一季度", value: res.data[0]},
          {name: "第二季度", value: res.data[1]},
          {name: "第三季度", value: res.data[2]},
          {name: "第四季度", value: res.data[3]},
        ]
        pieChart.setOption(pieOption)

      } else {
        this.$message.error("请求失败")
      }
    })

  },

}
</script>

<style scoped>

</style>