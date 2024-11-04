
// Generate xValues (months) and yValues (order counts) from orderCounts data
const xValues = orderCounts.map(order => order.month);
const yValues = orderCounts.map(order => order.count);

// Create the chart as a bar chart
new Chart("monthlyOrderChart", {
    type: "bar",
    data: {
        labels: xValues,
        datasets: [{
            backgroundColor: "pink",
            borderColor: "red",
            data: yValues
        }]
    },
    options: {
        legend: { display: false },
        scales: {
            yAxes: [{ ticks: { beginAtZero: true } }],
            xAxes: [{ barPercentage: 0.5 }] // Optional: controls the width of the bars
        },
        title: {
            display: true,
            text: "Monthly Seller Created"
        }
    }
});


