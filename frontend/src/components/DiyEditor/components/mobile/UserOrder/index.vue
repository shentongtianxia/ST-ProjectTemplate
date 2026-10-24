<template>
  <el-image :src="mitacLogo" />
</template>
<script setup lang="ts">
import { UserOrderProperty } from './config'
import mitacLogo from '@/assets/imgs/mitac-logo.png'

/** 用户订单 */
defineOptions({ name: 'UserOrder' })
// 定义属性
defineProps<{ property: UserOrderProperty }>()
</script>

<style scoped lang="scss"></style>
