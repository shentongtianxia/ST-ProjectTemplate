import { addCollection, getIcon } from '@iconify/vue'

// 菜单和图标选择器使用到对应图标时才加载，避免登录首屏下载完整的 Font Awesome 图标库。
const loaders = {
  fa: () => import('@iconify/json/json/fa.json'),
  'fa-solid': () => import('@iconify/json/json/fa-solid.json')
}
const pendingCollections = new Map<string, Promise<void>>()

export const isLazyLocalIcon = (name: string) => Object.hasOwn(loaders, name.split(':')[0])

export const loadLocalIcon = async (name: string) => {
  const prefix = name.split(':')[0] as keyof typeof loaders
  if (!getIcon(name) && Object.hasOwn(loaders, prefix)) {
    if (!pendingCollections.has(prefix)) {
      const pending = loaders[prefix]()
        .then(({ default: collection }) => {
          addCollection(collection)
        })
        .catch((error) => {
          pendingCollections.delete(prefix)
          throw error
        })
      pendingCollections.set(prefix, pending)
    }
    await pendingCollections.get(prefix)
  }
  return getIcon(name)
}
