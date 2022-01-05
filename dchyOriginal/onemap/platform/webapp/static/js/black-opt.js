$('.base-info-inner .toggle').click(function () {
    var child = $(this).children('span');
    if (child.hasClass('icon-angle-right')) {
        child.removeClass('icon-angle-right').addClass('icon-angle-left');
        $('.base-info-inner').animate(
            {'width':0},
            {duration:200, complete:function () {

            }});


    } else {
        child.addClass('icon-angle-right').removeClass('icon-angle-left');
        $('.base-info-inner').animate(
            {'width':220},
            { duration:200, complete:function () {
            }
            }
        );
    }
});