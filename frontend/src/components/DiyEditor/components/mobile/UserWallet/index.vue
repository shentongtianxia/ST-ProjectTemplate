<template>
  <el-image :src="mitacLogo" />
</template>
<script setup lang="ts">
import { UserWalletProperty } from './config'
import mitacLogo from '@/assets/imgs/mitac-logo.png'

/** 用户资产 */
defineOptions({ name: 'UserWallet' })
// 定义属性
defineProps<{ property: UserWalletProperty }>()
</script>

<style scoped lang="scss"></style>
