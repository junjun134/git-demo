<template>
  <div style="width: 100%">
    <el-carousel :interval="4000" height="400px" ref="carousel" @click.native="linkTo">
      <el-carousel-item  v-for="item in list" :key="item.url" >
        <img :action="'https://www.sqxy.edu.cn/'" :src="item.url" width="1000px" height="500px">
      </el-carousel-item>
    </el-carousel>
    <el-calendar v-model="value">
    </el-calendar>
  </div>
</template>

<script>
export default {
  name: "FrontHome",
  methods: {
    linkTo () {
      let activeIndex = this.$refs.carousel.activeIndex
      window.open(this.list[activeIndex].link,"_blank")
    },
    created() {
      this.refresh()
    },

  },
  data() {
    return {
      value: new Date(),
      list: [
        { url: require("../../assets/1.jpeg") ,link:"https://www.baidu.com/" },
        { url: require("../../assets/2.jpeg") ,link:"https://www.baidu.com/" },
        { url: require("../../assets/3.jpeg") ,link:"https://www.baidu.com/" },
        ]
    }
  },
}
</script>

<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
