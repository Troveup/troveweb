<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Profile section -->
<div class="profile-container">
  <div class="profile-pic-container"></div>
  <div class="username-container">
    <div class="username"><span>ORDERS AND TRY-ON MODELS</span></div>
    <div class="user-info-bar-container">
      <div class="nav" id="user-info-bar" style="padding-left: 40px">
        <ul>
          <li class="topnav"><a id="itemstab" href="#" class="pagee use-info-bar-text ${pageContext.request.requestURI eq '/WEB-INF/views/user/orders.jsp' ? 'current-page' : ' '}"><div class="user-info-count"><span class="user-info-count category">Current <span class="hidesmaal">Orders</span></span></div></a></li>
          <li class="topnav"><a id="followerstab" href="#" class="pagee use-info-bar-text"><div class="user-info-count"><span class="user-info-count category">Closed <span class="hidesmaal">Orders</span></span></div></a></li>
          <!-- <li class="topnav"><a id="followingtab" href="#" class="pagee use-info-bar-text"><div class="user-info-count"><span class="user-info-count category">Closed <span class="hidesmaal">Orders</span></span></div></a></li> -->
        </ul>
      </div>
    </div>
  </div>
</div>