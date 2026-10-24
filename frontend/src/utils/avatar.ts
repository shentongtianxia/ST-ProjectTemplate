const defaultAvatarThemes = [
  'mountain',
  'forest',
  'wave',
  'lake',
  'ginkgo',
  'sunflower',
  'rainbow',
  'moon',
  'waterfall',
  'dune'
] as const

export const DEFAULT_USER_AVATARS = defaultAvatarThemes.map((theme, index) => ({
  path: `/avatars/nature/nature-${String(index + 1).padStart(2, '0')}.webp`,
  labelKey: `profile.avatar.themes.${theme}`
}))

const defaultAvatarPattern = /^\/avatars\/nature\/nature-(?:0[1-9]|10)\.webp$/

// 兼容已有的空头像账号；新建账号由后端随机分配并持久化。
export const getDefaultUserAvatar = (userId: number): string => {
  const index = Math.abs(userId) % DEFAULT_USER_AVATARS.length
  return DEFAULT_USER_AVATARS[index].path
}

// 默认资源跟随前端站点部署路径；自定义头像地址原样保留。
export const resolveUserAvatar = (avatar: string): string =>
  defaultAvatarPattern.test(avatar) ? `${import.meta.env.BASE_URL}${avatar.slice(1)}` : avatar
