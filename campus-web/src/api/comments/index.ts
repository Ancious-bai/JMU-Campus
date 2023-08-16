import http from '../index.ts'

interface commentParams {
  postId: number
  content: string
  type: string
  rootId?: number
  toUserId?: number
}
// 添加评论
export function addComment(data: commentParams) {
  return http.request({
    method: 'post',
    url: '/comment/add',
    data
  })
}
// 点赞 | 取消点赞用户评论
export function likeComment(commentId: number, userId: number) {
  return http.request({
    url: '/comment/like',
    method: 'post',
    data: {
      commentId
    },
    headers: {
      userId
    }
  })
}

// 删除评论
export function deleteComment(commentId: number) {
  return http.request({
    method: 'post',
    url: '/comment/delete',
    data: {
      commentId
    }
  })
}
