<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Profile section -->
<div class="collection-container">
  
  <div class="profile-pic-container">
    <c:if test="${noFollowCollection}"><button class="btn btn--lightgray btn-md btn--edit--profile" data-toggle="modal" data-target="#modal-editCollection"><span style="color: #fff"><img src="https://storage.googleapis.com/trove-demo-cdn/img/edit-icon-white.svg" class="inline-icon-edit-sm sm">Edit Collection</span></button></c:if>
    <%--<c:if test="${not noFollowCollection}">
      <c:if test="${isFollowCollection}"><button onclick="followCollection('${collection.collectionId}')" id="fbuttonCollection" class="btn folbtn btn--red btn-md btn--edit--profile"><span style="color: #fff">Follow Collection</span></button></c:if>
      <c:if test="${not isFollowCollection}"> <button onclick="unfollowCollection('${collection.collectionId}')" id="ufbuttonCollection" class="btn ufolbtn btn--lightgray btn-md btn--edit--profile"><span style="color: #fff">Unfollow Collection</span></button></c:if>
    </c:if> --%>

    <div class="collection-user-info" style="float: left">
      <div class="made-by-pic"><a href="/private/user/${user.userId}" alt="view this user's profile"><img src="${user.fullProfileImagePath}" alt="user profile image" class="profile-pic-img"></a></div>
      <div class="made-by-username"><a href="/private/user/${user.userId}" alt="view this user's profile"><span style="position:relative; top:10px">${user.username}</span></a></div>
    </div>
  </div>
  <div class="username-container">

    <div class="username" id="collection-name" ><span>${collection.collectionName}</span></div>
    <div class="user-info-bar-container">
      <div class="nav" id="user-info-bar" style="padding-left: 40px">
        <ul>
          <li class="topnav"><a id="itemstab" href="/private/profile" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/collection.jsp' ? 'current-page' : ' '}" ><div class="user-info-count"><span class="item-count value">${collectionItemsCount}</span><span class="user-info-count category">Items</span></div></a></li>  
          <li class="topnav"><a id="followerstab" href="/private/followers" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/followers.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="user-info-count value">${collectionFollowersCount}</span><span class="user-info-count category">Followers</span></div></a></li>
        </ul>
      </div>
    </div>
  </div>
</div>


<div class="modal fade" id="modal-editCollection" tabindex="-1" role="dialog" aria-labelledby="newCollection" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header"><h4 class="modal-title" id="newCollection">Edit Collection</h4></div>
      <div class="modal-body">
        <form class="edit-collection form-horizontal" action='/' method='post'>
          <div class="form editCollection">
            <fieldset>
              <div class="col-xs-12">
                <label>Collection Name</label>
                <input type="text" id="editCollectionName" name="editCollectionName" class="form-control" placeholder="${collection.collectionName}" />
              </div>
              <div class="col-xs-12">
                <label>Collection Description</label>
                <textarea type="textarea" name="editCollectionDescription" row="3" id="editCollectionDescription" class="form-control" placeholder="${collection.collectionDescription}"></textarea>
              </div>
            </fieldset>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn" data-dismiss="modal" style="float: left;">Cancel</button>
        <button id="editCollection" onclick="updateCollection('${collection.collectionId}')" data-dismiss="modal" type="button" class="btn btn_save">Save</button>
      </div>
    </div>
  </div>
</div>