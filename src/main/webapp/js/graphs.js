
google.charts.load('current', {
    'packages': ['geochart'],
    // Note: you will need to get a mapsApiKey for your project.
    // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings
    'mapsApiKey': 'AIzaSyACKtCu3dA0WCAfWTcAJaJBzuaTGlGFhrk'
});
google.charts.setOnLoadCallback(drawMarkersMap);

function drawMarkersMap() {
    
    var zone =[['Zip', 'Occurances']];
    var input = $(".input-carte");
    for (i of input){
        var zip = $(i).attr("id");
        var ca = $(i).attr("value");
        zone.push([zip,ca]);
    }
    console.log(zone);
    var data = google.visualization.arrayToDataTable(zone);
    var options = {
        region: 'US',
        displayMode: 'markers',
        colorAxis: {colors: ['green', 'blue']}
    };

    var chart = new google.visualization.GeoChart(document.getElementById('carte_affichage'));
    chart.draw(data, options);
}