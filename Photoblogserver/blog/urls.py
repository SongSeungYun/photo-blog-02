from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import PostViewSet, object_stats, stats_page, date_stats, date_stats_page
from . import views

router = DefaultRouter()
router.register(r'posts', PostViewSet)

urlpatterns = [
    # HTML 페이지들은 html/ prefix로 이동
    path('html/', views.post_list, name='post_list'),
    path('html/post/<int:pk>/', views.post_detail, name='post_detail'),
    path('html/post/new/', views.post_new, name='post_new'),
    path('html/post/<int:pk>/edit/', views.post_edit, name='post_edit'),
    path('html/js_test/', views.js_test, name='js_test'),

    # ⭐ 그래프 페이지
    path('html/stats/', stats_page, name='stats_page'),
    path('html/date-stats/', date_stats_page, name='date_stats_page'),  # ★ 새로운 페이지
    path('object-stats/', object_stats, name='object_stats'),   # ★ 핵심
    path('date-stats/', date_stats, name='date_stats'),     # ★ 추가됨

    # DRF router
    path('', include(router.urls)),
    path('html/', views.post_list, name='post_list'),

]
