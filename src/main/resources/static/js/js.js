$(document).ready(function () {


    function addRowHandlers() {

        var table = document.getElementById("dtOrderExample");
        var rows = table.getElementsByTagName("tr");

        for (i = 0; i < rows.length; i++) {
            var currentRow = table.rows[i];

            var createClickHandler = function (row) {
                return function () {

                    // clear all backgound
                    for (i = 0; i < rows.length; i++) {
                        var currentRow = table.rows[i];
                        currentRow.style.backgroundColor = 'transparent';

                    }
                    // new selected Row
                    var cell = row.getElementsByTagName("td")[0];
                    var id = cell.innerHTML;
                    // alert("id:" + id);
                    row.style.backgroundColor = "yellow";
                };
            };
            currentRow.onclick = createClickHandler(currentRow);
        }
    }

    window.onload = addRowHandlers();

    // data tables Config
    $('#dtOrderExample').DataTable({
        "paging": false,
        "info": false,
        "searching": false
    });
    $('.dataTables_length').addClass('bs-select');


// ajax search Method

    $("#search-form").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();

    });

    function fire_ajax_submit() {
        var fileName = $("#inputSearch").val();
        console.log(fileName)
        var search = {}
        search["fileName"] = fileName;

        $("#searchButton").prop("disabled", true);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/search",
            data: JSON.stringify(search),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (data) {

                // get json response
                var json = JSON.parse(JSON.stringify(data));
                var result = json.result;
                var msg = json.msg;

                $("#searchButton").prop("disabled", false);

                // set Model Data
                $('#exampleModalLabel').text("File Name : " +result["fileName"]);
                $('#infoIdModal').text(result["infoId"]);
                $('#addingDateModal').text(result["addingDate"]);
                $('#processingDateModal').text(result["processingDate"]);
                $('#finishDateModal').text(result["finishDate"]);
                $('#statusModal').text(result["status"]);
                $('#countDocumentsModal').text(result["countDocuments"]);
                $('#countFoldersModal').text(result["countFolders"]);
                $('#typeModal').text(result["type"]);
                $('#threadNameModal').text(result["threadName"]);
                // show Modal
                $("#exampleModal").modal();
            },
            error: function (e) {
                var json = JSON.parse(e.responseText);

                $("#searchButton").prop("disabled", false);

                alert("Error " + json['msg'])

            }
        });

    }
});
