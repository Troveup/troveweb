<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Profile section -->
<div class="profile-container">
  <div class="cover-photo-container">
    <c:choose>
      <c:when test="${not empty canFollowUser}">
        <img id="covimg" src="${user.coverPhotoImagePath}">
      </c:when>
      <c:when test="${empty canFollowUser}">
        <img id="covimg" src="${authUser.coverPhotoImagePath}">
      </c:when>
      <c:otherwise></c:otherwise>
    </c:choose>
  </div>
  <div class="profile-pic-container">
    <c:choose>
      <c:when test="${not empty canFollowUser}">
        <!-- <c:if test="${canFollowUser}"><button onclick="TROVE.followUser(${user.userId})" id="folwbuttonprofile" class="btn folbtn${user.userId} btn--red btn-md btn--edit--profile"><span style="color: #fff">Follow</span></button></c:if>
        <c:if test="${!canFollowUser}"><button onclick="TROVE.unfollowUser(${user.userId})" id="folwbuttonprofile" class="btn ufolbtn${user.userId} btn--lightgray btn-md btn--edit--profile"><span style="color: #fff">Unfollow</span></button></c:if> -->
      </c:when>
      <c:when test="${empty canFollowUser}">
      <a onclick="editcoverphoto()" class="hidden-xs" id="covbutton" data-toggle="modal" href="" data-target="#myModal">Change Cover Photo</a>
      <!-- <a href="/account/settings"><button onclick="editprof()" class="btn btn--lightgray btn-md btn--edit--profile"><span style="color: #fff">Edit Profile</span></button></a> --></c:when>
      <c:otherwise></c:otherwise>
    </c:choose>
    <div class="profile-pic-holder">
      <img src="${user.fullProfileImagePath}" class="profile-pic-img">
    </div>
  </div>
  <div class="username-container">
    <div class="username"><span>${user.firstName}'s Trove</span><br>
    <c:choose>
      <c:when test="${not empty canFollowUser}">
        <c:if test="${canFollowUser}"><button onclick="TROVE.followUser(${user.userId})" id="folwbuttonprofile" class="btn folbtn${user.userId} btn--red btn-md" style="margin-top:10px;"><span style="color: #fff">Follow</span></button></c:if>
        <c:if test="${!canFollowUser}"><button onclick="TROVE.unfollowUser(${user.userId})" id="folwbuttonprofile" class="btn ufolbtn${user.userId} btn--lightgray btn-md" style="margin-top:10px;"><span style="color: #fff">Unfollow</span></button></c:if>
      </c:when>
      <c:when test="${empty canFollowUser}">
      <a href="/account/settings"><button onclick="editprof()" class="btn btn--lightgray btn-md" style="margin-top:10px;"><span style="color: #fff">Edit Profile</span></button></a></c:when>
      <c:otherwise></c:otherwise>
    </c:choose>
  </div>
    <div class="user-info-bar-container">
      <div class="nav" id="user-info-bar" style="padding-left: 40px">
        <ul>
          <c:choose>
            <c:when test="${not empty canFollowUser}">
              <li class="topnav"><a id="itemstab" href="/private/profile" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/profile.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="item-count value">${itemCount}</span><span class="user-info-count category">Items</span></div></a></li>  
              <li class="topnav"><a id="collectionstab" href="/public/collections" class="use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/collections.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="user-info-count value" id="collectCounter">${collectionCount}</span><span class="user-info-count category">Collections</span></div></a></li>
              <li class="topnav"><a id="followerstab" href="/private/followers" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/followers.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="folwng user-info-count value">${followersCount}</span><span class="user-info-count category">Followers</span></div></a></li>
              <li class="topnav"><a id="followingtab" href="/private/following" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/following.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="user-info-count value">${followedUserCount}</span><span class="user-info-count category">Following</span></div></a></li>
            </c:when>
            <c:when test="${empty canFollowUser}">
              <li class="topnav"><a id="itemstab" href="/private/profile" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/profile.jsp' ? 'current-page' : ' '}" ><div class="user-info-count"><span class="item-count value">${itemCount}</span><span class="user-info-count category">Items</span></div></a></li>  
              <li class="topnav"><a id="collectionstab" href="/public/collections" class="use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/collections.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="user-info-count value" id="collectCounter">${collectionCount}</span><span class="user-info-count category">Collections</span></div></a></li>
              <li class="topnav"><a id="followerstab" href="/private/followers" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/followers.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="user-info-count value">${followersCount}</span><span class="user-info-count category">Followers</span></div></a></li>
              <li class="topnav"><a id="followingtab" href="/private/following" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/following.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="folwng user-info-count value">${followedUserCount}</span><span class="user-info-count category">Following</span></div></a></li>
            </c:when>
            <c:otherwise></c:otherwise>
          </c:choose>
        </ul>
      </div>
    </div>
  </div>
</div>