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
                            'src'   : STORAGE_URL+obj.url
        }).appendTo( col );
    }

    $(document).ready(function() {
        $().ready(function() {

              loadPictures(0, 8);

              let addPicturesBtn = document.getElementById('add_pictures_btn');
              let pictures       = document.getElementById('pictures');

              //crop functions
              let upPicBtn      = document.getElementById('up_pic_wrap');
              let upPicInput    = document.getElementById('up_pic');
              let upPicImage    = document.getElementById('pic_cropped_img');
              let cropDialog    = document.getElementById('cropDialog');
              let upPicCancel   = document.getElementById('btn_up_pic_cancel');
              let croppedImg    = document.getElementById('inputCropImage');

              //init
              upPicImage.parentElement.style.display = 'none';

              upPicCancel.onclick = function() {
                upPicImage.parentElement.style.display = 'none';
                upPicBtn.style.display = 'flex';
              }

              function showCropDialog(){
                cropDialog.style.display = "block";
              }

              function hideCropDialog(){
                cropDialog.style.display = "none";
              }

              var _URL = window.URL || window.webkitURL;

              // Image cropping functions
              var jcrop;

              function initCropRect( imgSrc ){
                let rect = Jcrop.Rect.sizeOf(
                    imgSrc.naturalWidth,
                    imgSrc.naturalHeight
                );

                if(jcrop) jcrop.destroy();

                jcrop = Jcrop.attach( imgSrc.id, {
                    multi: false
                });

                jcrop.newWidget(rect.scale( .75, .75).center(imgSrc.naturalWidth, imgSrc.naturalHeight));

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
                    // cropped canvas
                    var getCropped = document.createElement('canvas');
                    var context = getCropped.getContext("2d");

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

                    return getCropped;

                }

                return -1;
              }

              croppedImg.onclick = function() {
                $(croppedImg).attr('src',  STORAGE_URL+updateItem.url);
                showCropDialog();
                initCropRect(croppedImg);
          }

          $('#up_pic_btn').click(function(event){
            $(croppedImg).attr('src', null);
            showCropDialog();
          });

          $('#closeCrop').click(function(event){
            hideCropDialog();
          });

          $('#cropSubmit').click(function(event){

            var image = new Image();
            image.crossOrigin = "Anonymous";
            image.src = croppedImg.src;

            image.onload = function(){
              if(jcrop) {
                // cropped canvas
                var cropped = document.createElement('canvas');
                var context = cropped.getContext("2d");

                context.drawImage(
                    image,
                    jcrop.active.pos.x * image.naturalWidth / image.width,
                    jcrop.active.pos.y * image.naturalHeight / image.height,
                    jcrop.active.pos.w * image.naturalWidth / image.width,
                    jcrop.active.pos.h * image.naturalHeight / image.height,
                    0,
                    0,
                    jcrop.active.pos.w * image.naturalWidth / image.width,
                    jcrop.active.pos.h * image.naturalHeight / image.height
                );
                // img, sx, sy, swidth, sheight, dx, dy, dwidth, dheight
                // s = source
                // d = destination

                upPicImage.src = cropped.toDataURL();

                upPicBtn.style.display = 'none';
                upPicCancel.style.display = 'inline-block';
                upPicImage.parentElement.style.display  = 'inline-block';

              }
            };

            hideCropDialog();
          });

          upPicInput.onchange = function () {
            $(croppedImg).attr('src', '');
            if (this.files && this.files[0]) {
                img = new Image();

                img.src = _URL.createObjectURL(this.files[0]);

                img.onload = function() {

                    if( getImgForCrop( this, 30, 30 ) != 0 ) hideCropDialog();

                }

                img.onerror = function() {
                    alert('not a valid file: ' + file.type);
                }
            }
          }
        });
    });
