<template>
  <div id="app">
<!--    <router-view/>-->
      <home-page/>
  </div>
</template>

<script>

import HomePage from '@/view/HomePage'

  export default {
    name: 'App',
      components: {HomePage}
  }
</script>

<style scoped lang="less">
  #app {
    height: 100%;
    position: relative;
    z-index: 0;
    background-color: #F3F5F7;
  }
</style>
