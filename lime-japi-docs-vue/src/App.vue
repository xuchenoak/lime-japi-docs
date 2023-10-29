<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>

  export default {
    name: 'App',
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
