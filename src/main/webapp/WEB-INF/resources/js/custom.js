$(document).ready(function() {

  // establish the pjax container
  // $(document).pjax('a', '#pjax-container');

	// add/remove active class from current nav link
  $('#filters').on( 'click', 'a', function( event ) {
    $('a').removeClass('active');
    $(this).addClass('active');
  });

  $('li').on( 'click', 'a', function( event ) {
    $('a').removeClass('active');
    $(this).addClass('active');
  });

  $('.jam').on('mouseover', function() {
    $('.dropdown-container.jam').addClass('showtime');
  });

  $('.jam').on('mouseleave', function() {
    $('.dropdown-container.jam').removeClass('showtime');
  });

  $('.pro').on('mouseover', function() {
    $('.dropdown-container.pro').addClass('showtime');
  });

  $('.pro').on('mouseleave', function() {
    $('.dropdown-container.pro').removeClass('showtime');
  });
  
});