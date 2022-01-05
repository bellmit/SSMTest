<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import { getUserInfo } from "./service/template"
export default {
  name: 'App',
  watch: {
  },
  created() {
    // this.getUserMessage();
  },
  mounted(){
    
  },
  methods: {
    getUserMessage(){
      getUserInfo().then(res => {
        sessionStorage.setItem("userInfo",JSON.stringify(res.data))
      })
    }
  },
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333;
  background: #f5f5f5;
  height: 100%;
}
</style>
