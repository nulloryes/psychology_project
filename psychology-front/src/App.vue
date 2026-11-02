<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  watch: {
    $route: {
      immediate: true,
      handler(to) {
        if (to.path === '/login' || to.path === '/register') {
          document.getElementById('dify-chatbot-script')?.remove();
          document.getElementById('dify-chatbot-bubble-button')?.remove();
          document.getElementById('dify-chatbot-bubble-window')?.remove();
        }
      },
    },
  },
};
</script>

<style lang="scss">
  * {
    padding: 0;
    margin: 0;
  }
  #dify-chatbot-bubble-button {
    background-color: #1C64F2 !important;
  }
  #dify-chatbot-bubble-window {
    width: 24rem !important;
    height: 40rem !important;
  }
</style>
