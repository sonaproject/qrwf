<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <title>Wifi auto connect</title>
    <link rel="shortcut icon" href="/img/favicon.ico" />
    <!-- CSS files -->
    <link href="/css/tabler.min.css" rel="stylesheet"/>
    <link href="/css/demo.min.css" rel="stylesheet"/>
    <style>
        @media print {
            body {
                width: 800px !important;
                height: 300px !important;
                margin: 0 auto;
                page-break-inside: avoid;
            }
        }
    </style>
</head>
<body  class=" border-top-wide border-primary d-flex flex-column" id="bodyDiv">
<div class="page page-center">
    <div class="container-tight">
        <div class="row">
            <div id="printMain">
            <div class="col-12 text-center bold" id="infoDiv">
                <img class="w-100" src="/img/wifi.png" />
                <h1 class="text-center bold" style="font-size: 50px;">카메라로 스캔 하세요!</h1>
            </div>

            <div id="inputDiv">
                <div class="m-3">
                    <label class="form-label" style="font-size: 20px; ">▶ QR코드의 상단에 들어갈 제목을 입력 하세요.</label>
                    <div class="input-group input-group-flat">
                        <input type="text" class="form-control border-lime" style="font-size: 20px;" value=""  placeholder="멋진 이름으로 입력하세요 ^^" id="titleInput"/>
                    </div>
                </div>

                <div class="m-3">
                    <label class="form-label" style="font-size: 20px; ">▶ Wifi 아이디를 입력 하세요.</label>
                    <div class="input-group input-group-flat">
                        <input type="text" class="form-control border-lime" style="font-size: 20px;" value=""  placeholder="ID" id="idInput"/>
                    </div>
                </div>
                <div class="m-3">
                    <label class="form-label" style="font-size: 20px; ">▶ Wifi 비밀번호를 입력 하세요.</label>
                    <div class="input-group input-group-flat">
                        <input type="text" class="form-control border-lime" style="font-size: 20px;" value=""  placeholder="Password" id="pwInput"/>
                    </div>
                </div>
                <div class="m-3">
                    <label class="form-label" style="font-size: 20px; ">▶ Wifi 보안 타입을 선택하세요. <small class="text-muted">기본은 WPA 입니다.</small></label>
                    <div class="input-group input-group-flat">
                        <select class="form-control border-lime" style="font-size: 20px;" id="typeSelect">
                            <option value="WPA" selected>WPA</option>
                            <option value="WPA2">WPA2</option>
                            <option value="WPA3">WPA3</option>
                            <option value="WPE">WPE</option>
                        </select>
                    </div>
                </div>
                <div class="m-3">
                    <button class="btn btn-info form-control fs-3" id="runBtn">QR코드 만들기 실행</button>
                </div>
            </div>
            <div class="col-12 d-none" id="printable">
                <div class="col-12 text-center bold">
                    <h1 class="text-center bold text-info" style="font-size: 40px;" id="resultTitle"></h1>
                    <img class="w-100" src=""  id="resultQrcode"/>
                </div>
            </div>
            </div>

        </div>
    </div>
</div>
<!-- Libs JS -->
<!-- Tabler Core -->
<script src="/js/jquery-3.6.4.js"></script>
<script src="/js/demo.min.js" defer></script>

<script>
    $("#runBtn").click(function (){
        let getTitle = $("#titleInput").val();
        let getId = $("#idInput").val();
        let getPw = $("#pwInput").val();
        let getType = $("#typeSelect").val();

        console.log(getTitle)
        $("#resultTitle").text(getTitle);
        let qrcodeUrl = 'https://chart.googleapis.com/chart?cht=qr&chs=500x500&chl=WIFI:';
        let createStr = 'S:'+getId+';T:'+getType+';P:'+getPw+';';

        $("#resultQrcode").attr('src',qrcodeUrl+createStr);
        $("#printable").removeClass('d-none');
        $("#inputDiv").addClass('d-none');
    });





</script>
</body>
</html>