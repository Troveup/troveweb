<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- Add to the end of the document to be sure that all Bootstrap javascript source is added -->
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> -->
<!-- <script src="/resources/js/bootstrap.min.js"></script> -->
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<!-- <script src="/resources/js/ie10-viewport-bug-workaround.js"></script> -->

<!-- Google Analytics Pageview Tracking Code -->
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
          m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-58023662-1', 'auto');
  ga('send', 'pageview');

</script>

<!--Fluid block arranging. Could be greatly condensed with more conditionals but I have no idea what I'm doing-->
<!--TODO:  Condense this JS-->
<!--TODO:  Move the JS out of this file and into its own JS file for import-->

<script>
    var colCount = 0;
    var colWidth = 250;
    var margin = 30;
    var spaceLeft = 0;
    var windowWidth = 0;
    var blocks = [];



  function setupBlocks() {
  windowWidth = $(window).width();
  blocks = [];
  //debugger;
  // Calculate the margin so the blocks are evenly spaced within the window
  colCount = Math.floor(windowWidth/(colWidth+margin*2));
  spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
  console.log(spaceLeft);
  
  for(var i=0;i<colCount;i++){
    blocks.push(margin);
  }
  positionBlocks();
}

  function setupBlocksWithBanner() {
  windowWidth = $(window).width();
  blocks = [];

  // Calculate the margin so the blocks are evenly spaced within the window
  colCount = Math.floor(windowWidth/(colWidth+margin*2));
  spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
  console.log(spaceLeft);
  
  for(var i=0;i<colCount;i++){
    blocks.push(margin);
  }
  positionBlocksWithBanner();
}

function positionBlocks() {
  $('.block').each(function(i){
    var min = Array.min(blocks);
    var index = $.inArray(min, blocks);
    var leftPos = margin+(index*(colWidth+margin));
    $(this).css({
      'left':(leftPos+spaceLeft)-40+'px',
      'top':min+130+'px',
      'display':'inherit'
    });
    blocks[index] = min+$(this).outerHeight()+margin;
  }); 
}

function positionBlocksWithBanner() {
  $('.block').each(function(i){
    var min = Array.min(blocks);
    var index = $.inArray(min, blocks);
    var leftPos = margin+(index*(colWidth+margin));
    $(this).css({
      'left':(leftPos+spaceLeft)-40+'px',
      'top':min+$(".browse-container").height()+$(".navbar").height()+$(".banner").height()+'px',
      'display':'inherit'
    });
    blocks[index] = min+$(this).outerHeight()+margin;
  }); 
}

function setupBlocksProfile() {
  windowWidth = $(window).width();
  blocks = [];

  // Calculate the margin so the blocks are evenly spaced within the window
  colCount = Math.floor(windowWidth/(colWidth+margin*2));
  spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
  console.log(spaceLeft);
  
  for(var i=0;i<colCount;i++){
    blocks.push(margin);
  }
  positionBlocksProfile();
}

function positionBlocksProfile() {
  $('.block').each(function(i){
    var min = Array.min(blocks);
    var index = $.inArray(min, blocks);
    var leftPos = margin+(index*(colWidth+margin));
    $(this).css({
      'left':(leftPos+spaceLeft)-40+'px',
      'top':min+$(".profile-container").height()+$(".header").height()+130+'px',
      'display':'inherit'
    });
    blocks[index] = min+$(this).outerHeight()+margin;
  }); 
}

function setupBlocksFeatured() {
  windowWidth = $(window).width();
  blocks = [];

  // Calculate the margin so the blocks are evenly spaced within the window
  colCount = Math.floor(windowWidth/(colWidth+margin*2));
  spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
  console.log(spaceLeft);
  
  for(var i=0;i<colCount;i++){
    blocks.push(margin);
  }
  positionBlocksFeatured();
}

function positionBlocksFeatured() {
  $('.block').each(function(i){
    var min = Array.min(blocks);
    var index = $.inArray(min, blocks);
    var leftPos = margin+(index*(colWidth+margin));
    $(this).css({
      'left':(leftPos+spaceLeft)-40+'px',
      'top':min+$(".banner-container").height()+$(".navbar").height()+10+'px',
      'display':'inherit'
    });
    blocks[index] = min+$(this).outerHeight()+margin;
  }); 
}

function setupBlocksCollection() {
  windowWidth = $(window).width();
  blocks = [];

  // Calculate the margin so the blocks are evenly spaced within the window
  colCount = Math.floor(windowWidth/(colWidth+margin*2));
  spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
  console.log(spaceLeft);
  
  for(var i=0;i<colCount;i++){
    blocks.push(margin);
  }
  positionBlocksCollection();
}

function positionBlocksCollection() {
  $('.block').each(function(i){
    var min = Array.min(blocks);
    var index = $.inArray(min, blocks);
    var leftPos = margin+(index*(colWidth+margin));
    $(this).css({
      'left':(leftPos+spaceLeft)-40+'px',
      'top':min+420+'px',
      'display':'inherit'
    });
    blocks[index] = min+$(this).outerHeight()+margin;
  }); 
}

// Function to get the Min value in Array
Array.min = function(array) {
    return Math.min.apply(Math, array);
};
  </script>


<script>
$('.modal').on('shown.bs.modal', function() {
  // $(this).find('[autofocus]').focus();
});
</script>
