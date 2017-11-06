var dom;
Vue.component('my-test',
    {
        /*template: '<div id="footer"></div>',*/
        beforeCreate: function () {
            Ajx.get('/ui/footer', {responseType: 'text/html'}).then(function (response) {
                document.getElementById("footer").innerHTML = response.data;
            });
        }
    }
);

/*
*/
