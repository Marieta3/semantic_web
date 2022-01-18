
var save_rdf_url = "api/saveRDF";
var query_url = "api/query/";

$(document).ready(function(){
    console.log('ready');
});

$(document).on('click','#saveRDF',function(e){
    console.log("blabla")
        e.preventDefault()
        $.ajax({
            type:'GET',
            url:save_rdf_url,
            success:function(data){
                console.log("bla")
            }
        })
});

$(document).on('click','.query-btn',function(e){
        e.preventDefault()
        $.ajax({
            type:'GET',
            url:query_url+this.id,
            success:function(data){
                console.log(data);
                const body = document.body,
                  tbl = document.createElement('table');
                  tbl.style.width = '100px';
                  //tbl.style.border = '1px solid black';
                  tbl.setAttribute('id', 'table-content');
                  tbl.classList.add("table");
                  tbl.classList.add("table-striped");
                  tbl.classList.add("table-hover");
                  var has_th = false;
                  var header = null;
                  var tbody = null;
                  var cnt = 1;
                  $.each(data, function(index, item){
                    var tr = null;

                    var t_row = null;

                    //const tr = null;
                    if(!has_th){
                        console.log("nema header1")
                        header = tbl.createTHead();
                        header.classList.add("thead-dark");
                        t_row = header.insertRow();
                        th_d1 = t_row.insertCell();
                        th1 = document.createElement("th");
                        th1.innerHTML = "#";
                        th_d1.appendChild(th1);
                        tbody = tbl.createTBody();

                        tr = tbody.insertRow();
                        td1 = tr.insertCell();
                        td1.appendChild(document.createTextNode(index));

                    }else{
                        console.log("ima header1");
                        // body = tbl.createTBody();

                        tr = tbody.insertRow();
                        td1 = tr.insertCell();
                        td1.appendChild(document.createTextNode(index));
                    }
                    $.each(item, function(index1, item1){
                        // console.log(index1)
                        var th_d = null;
                        var td = null;
                        if(!has_th){
                            console.log("nema header2");
                            th_d = t_row.insertCell();
                            th = document.createElement("th");
                            th.innerHTML = index1;
                            th_d.appendChild(th);
                            ///th_d.innerHTML(index1);
                            //th_d.style.border = '1px solid black';
                            th_d.setAttribute("scope", "col")
                            td = tr.insertCell();
                            td.appendChild(document.createTextNode(item1));
                        }else{
                            console.log("ima header2")
                            td = tr.insertCell();
                            td.appendChild(document.createTextNode(item1));
                            //td.style.border = '1px solid black';
                        }
                    })
                    has_th = true;
                  })
                  $(document).find("#table-content").remove()
                  body.appendChild(tbl);
            }
        })
});