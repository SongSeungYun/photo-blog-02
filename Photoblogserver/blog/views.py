# blog/views.py
from django.shortcuts import render, get_object_or_404, redirect
from django.utils import timezone
from .models import Post
from .forms import PostForm
from rest_framework import viewsets
from .serializers import PostSerializer


# ──────────────────────────────
# 1️⃣ Django REST Framework API ViewSet
# ──────────────────────────────
class PostViewSet(viewsets.ModelViewSet):
    """
    /api_root/posts/ 엔드포인트 제공
    GET - 게시물 목록 조회
    POST - 새 게시물(이미지 포함) 등록
    """
    queryset = Post.objects.all().order_by('-published_date')
    serializer_class = PostSerializer
        # 생성 시 자동으로 author = 요청한 유저
    def perform_create(self, serializer):
        serializer.save(
            author=self.request.user,
            published_date=timezone.now()
        )

# ──────────────────────────────
# 2️⃣ Django 템플릿용 뷰 함수
# ──────────────────────────────
def post_list(request):
    posts = Post.objects.all().order_by('-created_date')

    return render(request, 'blog/post_list.html', {'posts': posts})


def post_detail(request, pk):
    post = get_object_or_404(Post, pk=pk)
    return render(request, 'blog/post_detail.html', {'post': post})


def post_new(request):
    if request.method == "POST":
        form = PostForm(request.POST, request.FILES)  # ✅ 이미지 포함
        if form.is_valid():
            post = form.save(commit=False)
            post.author = request.user
            if not post.published_date:
                post.published_date = timezone.now()
            post.published_date = timezone.now()
            post.save()
            return redirect('post_detail', pk=post.pk)
    else:
        form = PostForm()
    return render(request, 'blog/post_edit.html', {'form': form})


def post_edit(request, pk):
    post = get_object_or_404(Post, pk=pk)
    if request.method == "POST":
        form = PostForm(request.POST, request.FILES, instance=post)  # ✅ 이미지 포함
        if form.is_valid():
            post = form.save(commit=False)
            post.author = request.user
            if not post.published_date:
                post.published_date = timezone.now()
            post.published_date = timezone.now()
            post.save()
            return redirect('post_detail', pk=post.pk)
    else:
        form = PostForm(instance=post)
    return render(request, 'blog/post_edit.html', {'form': form})
def js_test(request):
    return render(request, 'blog/js_test.html')

from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from django.db.models import Count

@api_view(['GET'])
def post_stats(request):
    """
    YOLO 감지된 객체별 감지 횟수 집계 API
    """
    stats = (
        Post.objects.values('title')   # title 필드(객체명) 기준 그룹화
        .annotate(count=Count('title'))  # 그룹별 개수 세기
        .order_by('-count')             # 많이 감지된 순으로 정렬
    )

    return Response(stats)

def stats_page(request):
    return render(request, "blog/stats.html")

# ------------------------------------
# YOLO 객체 감지 통계 API (JSON 반환)
# ------------------------------------
from django.http import JsonResponse

def object_stats(request):
    stats = {}
    posts = Post.objects.all()

    # 제목(title) 기준 감지 횟수 집계
    for post in posts:
        if post.title:
            stats[post.title] = stats.get(post.title, 0) + 1

    return JsonResponse({
        "names": list(stats.keys()),
        "counts": list(stats.values())
    })


# ------------------------------------
# 통계 그래프 페이지 렌더링
# ------------------------------------
def stats_page(request):
    return render(request, "blog/stats.html")

from django.db.models import Count
from django.db.models.functions import TruncDate

def date_stats(request):
    # 날짜별 게시물 개수 그룹핑
    stats = (
        Post.objects
        .annotate(date=TruncDate('created_date'))
        .values('date')
        .annotate(count=Count('id'))
        .order_by('date')
    )

    # JSON 변환
    result = {str(item["date"]): item["count"] for item in stats}

    return JsonResponse(result)

def date_stats_page(request):
    return render(request, "blog/date_stats.html")