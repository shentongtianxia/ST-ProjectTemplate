<script lang="ts" setup>
import { propTypes } from '@/utils/propTypes'
import { getIcon, Icon as IconifyIcon } from '@iconify/vue'
import { useDesign } from '@/hooks/web/useDesign'
import { isLazyLocalIcon, loadLocalIcon } from '@/plugins/svgIcon/lazyCollections'

defineOptions({ name: 'Icon' })

const { getPrefixCls } = useDesign()

const prefixCls = getPrefixCls('icon')

const props = defineProps({
  // icon name
  icon: propTypes.string,
  // icon color
  color: propTypes.string,
  // icon size
  size: propTypes.number.def(16),
  // icon svg class
  svgClass: propTypes.string.def('')
})

const isLocal = computed(() => props.icon?.startsWith('svg-icon:'))

const symbolId = computed(() => {
  return unref(isLocal) ? `#icon-${props.icon.split('svg-icon:')[1]}` : props.icon
})

const getSvgClass = computed(() => {
  const { svgClass } = props
  return `iconify ${svgClass}`
})

const iconData = shallowRef<ReturnType<typeof getIcon> | string>()
watch(
  symbolId,
  async (name) => {
    if (isLocal.value || !name) {
      iconData.value = undefined
      return
    }
    // 本地图标在数据就绪前不交给 Iconify，避免额外向外部图标服务发请求。
    iconData.value = getIcon(name) || (isLazyLocalIcon(name) ? undefined : name)
    if (isLazyLocalIcon(name) && !iconData.value) {
      try {
        const data = await loadLocalIcon(name)
        if (symbolId.value === name) iconData.value = data
      } catch (error) {
        console.error('加载本地图标失败', error)
      }
    }
  },
  { immediate: true }
)
</script>

<template>
  <ElIcon :class="prefixCls" :color="color" :size="size">
    <svg v-if="isLocal" :class="getSvgClass">
      <use :xlink:href="symbolId" />
    </svg>

    <IconifyIcon
      v-else-if="iconData"
      :icon="iconData"
      :class="getSvgClass"
      :style="{ fontSize: `${size}px`, color }"
    />
  </ElIcon>
</template>
