/* 1. How many territories in each region? */
select regionDescription, count(territoryID) as num_territories 
    from territories, region 
    where territories.RegionId = region.RegionID 
    group by region.RegionID;

/* 2. Which products need to be re-ordered? */
select ProductID, ProductName, UnitsInStock, ReorderLevel 
	from products 
	where UnitsInStock <= ReorderLevel and UnitsOnOrder = 0 and Discontinued = 0;

/* 3. Which orders have not yet been shipped? */
select OrderID 
	from orders 
    where ShippedDate is null;
    
/* 4. Which orders were shipped to a city different than the city of the customer’s headquarters? */
select OrderID, ShipCity, City 
    from orders, customers 
    where orders.CustomerID = customers.CustomerID and ShipCity != City;

/* Alternate */
select OrderID, ShipCity, City 
    from orders join customers using (CustomerID) 
    where ShipCity != City;
    
/* 5. How many orders were sent by each shipper? */
select CompanyName, count(OrderID) as orders_sent 
    from orders, shippers 
    where ShipperID = ShipVia group by ShipperID;

/* 6. How many customers did each employee get an order from in the first quarter of 1998? */
select employees.EmployeeID, FirstName, LastName, count(OrderID) as num_orders 
    from employees, orders 
    where employees.EmployeeID = orders.EmployeeID 
        and OrderDate between '1998-01-01' and '1998-03-31'
    group by employees.EmployeeID;

/* 7. What is the cost of order 10256 */
select sum(UnitPrice * Quantity) as order_cost 
    from orderdetails 
    where OrderId = 10256;

/* 8. What is the total value of orders in 1997 that was sent via each shipper? */
select CompanyName, sum(order_value) as value_of_orders 
    from 
      shippers, 
      (select ShipVia, sum(UnitPrice * Quantity) as order_value 
        from orders natural join orderdetails 
        where year(OrderDate) = "1997" 
        group by orders.OrderID) as orderinfo 
    where ShipperID = ShipVia group by ShipperID;

/* Alternate */ 
select CompanyName, sum(UnitPrice * Quantity) as value_of_orders 
    from orders join orderdetails using (OrderID) join shippers on ShipVia = ShipperID
    where year(OrderDate) = "1997" group by ShipVia;

/* 9.1. What are the 3 most ordered categories of product by number of units? */
with orderinfo as 
  (select CategoryID, sum(Quantity) as order_count 
      from products join orderdetails using(productid) 
      group by CategoryID)

select CategoryName, sum(order_count) as num_ordered 
  from categories natural join orderinfo 
  group by CategoryID 
  order by num_ordered  desc limit 3;

/* Alternate */
select CategoryName, sum(Quantity) as num_ordered
    from orders join orderdetails using (OrderID) join products using (ProductID) join categories using (CategoryID)
    group by CategoryID
    order by num_ordered desc
    limit 3;

/* 9.2. What are the 3 most ordered categories of product by value of sales? */
with orderinfo as 
  (select CategoryID, sum(Quantity*orderdetails.UnitPrice) as orderitem_value 
      from products join orderdetails using (productid) 
      group by CategoryID)

select CategoryName, sum(orderitem_value) as order_value 
    from categories natural join orderinfo 
    group by CategoryID 
    order by order_value  desc limit 3;

/* Alternate */
select CategoryName, sum(Quantity * orderdetails.UnitPrice) as order_value
    from orders join orderdetails using (OrderID) join products using (ProductID) join categories using (CategoryID)
    group by CategoryID
    order by order_value desc
    limit 3;

/* 10. Who is the top salesperson in the fourth quarter of 1997? */
with sales_info as 
  (select EmployeeID, sum(UnitPrice * Quantity) as sale_value 
      from orders natural join orderdetails 
      where OrderDate between '1997-10-01' and '1997-12-31' 
      group by EmployeeID)

select EmployeeID, FirstName, LastName, sum(sale_value) as Sales 
  from employees natural join sales_info 
  group by EmployeeID 
  order by Sales desc 
  limit 1;

/* Alternate */
select EmployeeID, FirstName, LastName, sum(Quantity * orderdetails.UnitPrice) as Sales
	from orders join orderdetails using (OrderID) join employees using (EmployeeID)
	where OrderDate between "1997-10-01" and "1997-12-31"
	group by EmployeeID
	order by Sales desc
    limit 1;


/* 11. How many people directly report to each of the supervisors? */
with report_info as 
  (select ReportsTo, count(employeeID) as direct_reports 
      from employees 
      group by ReportsTo)

select EmployeeID, FirstName, LastName, direct_reports 
    from employees, report_info 
    where employees.EmployeeID = report_info.ReportsTo;

/* Alternate */
select ReportsTo as EmployeeID, FirstName, LastName, count(EmployeeID) as direct_reports
    from employees
    group by ReportsTo;

/* 12. Which customers bought more than $5000 in products in 1997 that could be traced back to a single supplier? */
with customer_supplier_info as 
  (select CustomerID, sum(UnitPrice * Quantity) as supplier_value, CompanyName
      from orders natural join orderdetails natural join products natural join suppliers
      where year(OrderDate) = 1997
      group by CustomerID, SupplierID
  )
  
select customers.CompanyName as customer, 
          customer_supplier_info.CompanyName as supplier, supplier_value 
  from customers, customer_supplier_info 
  where customers.CustomerID = customer_supplier_info.CustomerID 
          and supplier_value >= 5000;

/* Alternate */
select customers.CompanyName as customer, suppliers.CompanyName as supplier, sum(Quantity*orderdetails.UnitPrice) as supplier_value
	from orders join orderdetails using (OrderID) 
				join customers using (CustomerID) 
				join products using (ProductID)
                join suppliers using (SupplierID)
	where year(OrderDate) = 1997
    group by SupplierID, CustomerID
    having supplier_value >= 5000;