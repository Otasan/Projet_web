
google.charts.load('current', {
    'packages': ['geochart', 'corechart', 'bar'],
    // Note: you will need to get a mapsApiKey for your project.
    // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings
    'mapsApiKey': //maps API Key ici Demandez moi si vous la voulez -Alex
});
google.charts.setOnLoadCallback(drawMarkersMap);
//google.charts.setOnLoadCallback(drawStacked);
google.charts.setOnLoadCallback(drawChart);

function drawMarkersMap() {

    var zone = [];
    zone.push(['Zip', "Pourcentage du Chiffre affaire"]);
    var input = $(".input-carte");
    var caTotal = $("#chiffreAffaire").attr("value");
    for (i of input) {
        var zip = $(i).attr("id");
        var ca = 100 * $(i).attr("value") / caTotal;
        zone.push([zip.toString(), ca]);
    }
    console.log(zone);
    var data = google.visualization.arrayToDataTable(zone);
    var options = {
        region: 'US',
        displayMode: 'markers',
        sizeAxis: { minValue: 0, maxValue: 100 },
        colorAxis: {colors: ['red', 'blue']}
    };

    var chart = new google.visualization.GeoChart(document.getElementById('carte_affichage'));
    chart.draw(data, options);
}

/*function drawStacked() {
    var prod = [];
    prod.push(['Type Produit', "Pourcentage du Chiffre affaire"]);
    var input = $(".input-bar");
    var caTotal = $("#chiffreAffaire").attr("value");
    for (i of input) {
        var type = $(i).attr("id");
        var ca = 100 * $(i).attr("value") / caTotal;
        prod.push([type, ca]);
    }
    var data = google.visualization.arrayToDataTable(prod);

    var options = {
        title: "Pourcentage du chiffre d'affaire par type de produit" ,
        chartArea: {width: '50%'},
        isStacked: true,
        hAxis: {
            title: 'Pourcentage',
            minValue: 0
        },
        vAxis: {
            title: 'Type de produit'
        }
    };
    var chart = new google.visualization.BarChart(document.getElementById('bar_affichage'));
    chart.draw(data, options);
}*/

      
function drawChart() {
    var prod = [];
    prod.push(['Type Produit', "Pourcentage du Chiffre affaire"]);
    var input = $(".input-pie");
    var caTotal = $("#chiffreAffaire").attr("value");
    for (i of input) {
        var type = $(i).attr("id");
        var ca = 100 * $(i).attr("value") / caTotal;
        prod.push([type, ca]);
    }
    console.log(prod);
    var data = google.visualization.arrayToDataTable(prod);

    var options = {
        title: "Pourcentage du chiffre d'affaire par type de produit",
        pieHole: 0.4
    };

    var chart = new google.visualization.PieChart(document.getElementById('pie_affichage'));
    chart.draw(data, options);
}