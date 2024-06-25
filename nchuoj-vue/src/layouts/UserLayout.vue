<script setup lang="ts">
import { RouterView } from 'vue-router';
</script>

<template>
    <div id="userLayout">
        <a-layout style="min-height: 100vh;width: 100%;">
            <a-layout-header class="header">
                <div class="title-bar">
                    <a-space>
                        <div class="title">NCHU</div>
                        <img class="logo" src="../assets/image.png">
                    </a-space>
                </div>
            </a-layout-header>
            <a-layout-content class="context">
                <a-row class="grid-demo">
                    <a-col :span="12">
                        <img style="width: 100%;" src="../assets/code.jpg" />
                    </a-col>
                    <a-col :span="12">
                        <RouterView />
                    </a-col>
                </a-row>

            </a-layout-content>
            <a-layout-footer class="footer">南昌航空大学OJ判题系统</a-layout-footer>
        </a-layout>

    </div>
</template>

<style scoped>
#userLayout {

    text-align: center;
    display: flex;
}

#userLayout .title-bar {
    align-items: center;
}

#userLayout .logo {
    height: 48px;
}

#userLayout .title {
    color: #000000;
    margin-left: 16px;
    font-size: 25px;
}

#userLayout .header {
    padding: 20px;
}

#userLayout .context {
    padding: 20px;
}

#userLayout .footer {
    position: sticky;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
}
</style>