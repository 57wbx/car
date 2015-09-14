/**
 * zw
 */
app.filter('trustHtmlFilter', function ($sce) {

        return function (input) {

            return $sce.trustAsHtml(input);

        }

    });