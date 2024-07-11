var updatePic = {};

function loadPictures(page, size){
        $.ajax({
            url: '/pictures/page/'+page+'/'+size,
            type: 'GET',
            success: function (data) {
                console.log("Success:\n" + JSON.stringify(data));

                $(pictures).html(''); //clear items container

                $(data.content).each( function(i, obj){
                    addPictureItem( i, obj )
                });

                let page = data.page;
                row = $('<div/>', { 'class': 'row' }).appendTo( pictures );
                col = $('<div/>', { 'class': 'col-md-auto mr-auto ml-auto' }).appendTo( row );
                ul = $('<ul/>', { 'class': 'pagination pagination-info' }).appendTo( col );
                for( var i = 0; i < page.totalPages; i++ ) {
                    if( i == page.number ) {
                        li = $('<li/>', { 'class': 'active page-item'}).appendTo( ul );
                    } else {
                        li = $('<li/>', { 'class': 'page-item'}).appendTo( ul );
                    }

                    a = $('<a/>', { 'class'     : 'page-link',
                                    'onclick'   : 'loadPictures('+i+', 8);'
                    }).appendTo( li );

                    a.html(i+1);

                }
            },
            error: function (err) {
                //show error message
                console.log(err.statusText+"\n");
            }
        });
    }

    function addPictureItem( i, obj ) {
        if( i % 4 == 0 ) {
            row = $('<div/>', {'class': 'row'}).appendTo( $(pictures) );
        }

        col = $('<div/>', { 'id':     'picture_'+obj.id,
                            'class':  'col ml-auto ar-auto'}
        ).appendTo( row );

        img = $('<img/>', { 'class' : 'item-container rounded pointer',
                            'style' : 'inline-block',
                            'height': 'auto',
                            'src'   : STORAGE_URL+obj.path
        }).appendTo( col );
    }

    $(document).ready(function() {
        $().ready(function() {

              loadPictures(0, 8);

              let addPicturesBtn = document.getElementById('add_pictures_btn');
              let pictures       = document.getElementById('pictures');

              //crop functions
              let upPicBtn       = document.getElementById('up_pic_wrap');
              let upPicInput     = document.getElementById('up_pic');
              let upPicImage     = document.getElementById('pic_cropped_img');
              let cropDialog     = document.getElementById('cropDialog');
              let btnUpPic       = document.getElementById('btn_up_pic');
              let upPicCancel    = document.getElementById('btn_up_pic_cancel');
              let croppedImg     = document.getElementById('inputCropImage');
              let submitCropBtn  = document.getElementById("cropSubmit");
              let closeCropBtn   = document.getElementById("closeCrop");
              let upPicCancelBtn = document.getElementById("up_pic_cancel");

              upPicCancel.onclick = function() {
                  if(jcrop) jcrop.destroy();
                  canvas = null;

                  updatePic.files = null;
                  upPicInput.value = null;

                  upPicImage.style.display = 'none';
                  croppedImg.src = '';
                  upPicBtn.style.display = 'flex';
                  upPicCancelBtn.style.display = 'none';
              }

              closeCropBtn.onclick = function() {
                  if(jcrop) jcrop.destroy();
                  canvas = null;
              }

              upPicCancelBtn.onclick = function() {
                updatePic.files = null;
                upPicInput.value = null;
                canvas = null;

                upPicImage.style.display = 'none';
                croppedImg.src = '';
                upPicBtn.style.display = 'flex';
                upPicCancelBtn.style.display = 'none';
              }

              function showCropDialog(){
                cropDialog.style.display = "block";
              }

              function hideCropDialog(){
                cropDialog.style.display = "none";
              }

              // Image cropping functions
              var jcrop, canvas;

              function initCropRect( imgSrc ){

                let rect = Jcrop.Rect.sizeOf(
                    imgSrc.naturalWidth,
                    imgSrc.naturalHeight
                );

                if(jcrop) jcrop.destroy();

                jcrop = Jcrop.attach( imgSrc.id, {
                    multi: false
                });

                widget = jcrop.newWidget(rect.scale( .75, .75).center(imgSrc.naturalWidth, imgSrc.naturalHeight));

                jcrop.listen('crop.change',function(widget,e){
                    const pos = widget.pos;
                    if( pos.w < minSize.w ) pos.w = minSize.w;
                    if( pos.h < minSize.h ) pos.h = minSize.h;
                    if( pos.w > maxSize.w ) pos.w = maxSize.w;
                    if( pos.h > maxSize.h ) pos.h = maxSize.h;

                    widget.render();
                });

                jcrop.focus();
              }

              /**
              *  fill imgSrc dom <img> element from fileResult.
              *  Init Jcrop entity with selected rectangle with size > (minCropW, minCropH)
              *  If size of jcrop rectangle < than (minCropW, minCropH) then return -1, else
              *  create and focus on selected area and return 0.
              */
              function getImgForCrop( fileResult, minCropW, minCropH ){

                if(fileResult.width <= minCropW && fileResult.height <= this.minCropH){
                    alert('Wrong image size. Must be more than '+minCropW+' x '+minCropH);

                    return -1;
                }

                croppedImg.src = fileResult.src;
                initCropRect( croppedImg );

                return 0;

              }

              function cropImage(imgSrc){
                if(jcrop) {

                    let scaleW = parseFloat( imgSrc.naturalWidth / imgSrc.width );
                    let scaleH = parseFloat( imgSrc.naturalHeight / imgSrc.height );

                    // cropped canvas
                    let cropped = document.createElement('canvas');

                    cropped.width  = jcrop.active.pos.w * scaleW;
                    cropped.height = jcrop.active.pos.h * scaleH;

                    var context = cropped.getContext("2d");

                    context.drawImage(
                        imgSrc,
                        jcrop.active.pos.x * imgSrc.naturalWidth / imgSrc.width,
                        jcrop.active.pos.y * imgSrc.naturalHeight / imgSrc.height,
                        jcrop.active.pos.w * imgSrc.naturalWidth / imgSrc.width,
                        jcrop.active.pos.h * imgSrc.naturalHeight / imgSrc.height,
                        0,
                        0,
                        jcrop.active.pos.w * imgSrc.naturalWidth / imgSrc.width,
                        jcrop.active.pos.h * imgSrc.naturalHeight / imgSrc.height
                    );
                    // img, sx, sy, swidth, sheight, dx, dy, dwidth, dheight
                    // s = source
                    // d = destination

                    return cropped;

                }

                return -1;
              }

              btnUpPic.onclick = function() {

                if( canvas == null ) return;

                var fullPath = upPicInput.value;
                if (fullPath) {

                    let fileName = fullPath.split(/(\\|\/)/g).pop()

                    let file = null;
                    let formData = new FormData();

                    let headers = {};
                    headers[CSRF_HEADER_NAME] = CSRF_TOKEN;

                    let blob = canvas.toBlob(function(blob) {
                        file = new File([blob], fileName, { type: 'image/png' });
                        formData.append("files", file);

                        $(btnUpPic).prop("disabled", true);

                        $.ajax({
                            url: '/pictures',
                            type: 'POST',
                            data: formData,
                            headers: headers,
                            contentType: 'multipart/form-data',
                            processData : false, // prevent jQuery from automatically
                            contentType : false,
                            success: function (data) {
                                console.log("success: \n" + JSON.stringify(data));

                                loadPictures(0, 8);

                                $(btnUpPic).prop("disabled", false);

                                $(upPicCancel).click();
                            },
                            error: function (e) {
                                $(btnUpPic).prop("disabled", false);

                                //show error message
                                console.log('Error [ ' + e.status + ' : ' + e.statusText + ' ]');

                                alert(e.statusText);
                            }
                        });
                    }, 'image/png');
                }
              }

          $('#up_pic_btn').click(function(event){
            $(croppedImg).attr('src', null);
            showCropDialog();
          });


        submitCropBtn.onclick = function(){
             var image = new Image();
             image.crossOrigin = "Anonymous";
             image.src = croppedImg.src;
             image.width = croppedImg.width;
             image.height = croppedImg.height;
             image.naturalWidth = croppedImg.naturalWidth;
             image.naturalHeight = croppedImg.naturalHeight;

             image.onload = function(){
               if(jcrop) {
                 canvas = cropImage(this);
                 if( canvas != -1 ){
                   upPicImage.src = canvas.toDataURL();

                   upPicImage.style.display = 'inline-block';
                   upPicBtn.style.display = 'none';
                   upPicCancelBtn.style.display = 'block';

                   if(jcrop) jcrop.destroy();
                   //hide crop dialog
                   hideCropDialog();
                 }
               }
             }
        };

        function checkSize( fileResult, min, max ){

            if(fileResult.width < min.w || fileResult.height < min.h){
              alert('Wrong image size. Must be more than ' + min.w + ' x ' + min.h);

              return -1;
            }

            if(fileResult.width > max.w || fileResult.height > max.h){
              alert('Wrong image size. Must be smaller than ' + max.w + ' x ' + max.h);

              return -1;
            }

            return fileResult.src;
        }

        const EMPTY_IMG_SRC_URL = null;

        var minSize = Jcrop.Rect.sizeOf(200, 200);
        var maxSize = Jcrop.Rect.sizeOf(2000, 2000);

        upPicInput.onchange = function(){
            var _URL = window.URL || window.webkitURL;

            if (this.files && this.files[0]) {
                var fileTypes = ['jpg', 'jpeg', 'png'];
                var extension = this.files[0].name.split('.').pop().toLowerCase(),  //file extension from input file
                isSuccess = fileTypes.indexOf(extension) > -1;  //is extension in acceptable types

                if (!isSuccess) {
                  closeCropBtn.click();
                  alert("Wrong file type!");
                  return;
                }

                $(submitCropBtn).removeAttr('disabled');

                img = new Image();
                img.src = _URL.createObjectURL(this.files[0]);

                img.onload = function() {
                  result_src = checkSize( img, minSize, maxSize );
                  if( result_src != -1 ) {
                    //show crop dialog
                    croppedImg.src = result_src;
                    initCropRect( croppedImg );
                  } else {
                    //hide crop dialog
                    closeCropBtn.click();
                  }
                }

                img.onerror = function() {
                  alert('not a valid file: ' + file.type);
                  //hide crop dialog
                  closeCropBtn.click();
                }
              }
            }
        });
    });
