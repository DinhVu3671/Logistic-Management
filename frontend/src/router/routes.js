import React from 'react';
import DetailCustomer from '../components/customers/DetailCustomer'
import Customers from '../components/customers/Customers'
import Depots from '../components/depots/Depots'
import DetailDepot from '../components/depots/DetailDepot'
import DetailVehicle from '../components/vehicles/DetailVehicle'
import Vehicles from '../components/vehicles/Vehicles'
import Products from '../components/products/Products'
import Orders from '../components/orders/Orders'
import DetailOrder from '../components/orders/DetailOrder'
import SolutionRoute from '../components/solution/SolutionRoute'
import RouteInitialization from '../components/solution/init-route/RouteInitialization'
import DeliveryPlans from '../components/solution/delivery-plan/DeliveryPlans'
import TrackingRoute from '../components/solution/tracking-route/TrackingRoute';
import Login from '../views/dashboard/login/Login';
import GoodsGroups from '../components/goodsGroup/GoodsGroups'

const routes = [
  { path: '/', exact: true, name: 'Home' },
  { path: '/customers', exact: true, name: 'Customers', component: Customers },
  { path: '/depots', exact: true, name: 'Depots', component: Depots },
  { path: '/vehicles', exact: true, name: 'Vehicles', component: Vehicles },
  { path: '/products', exact: true, name: 'Products', component: Products },
  { path: '/orders', exact: true, name: 'Orders', component: Orders },
  { path: '/orders/detail', exact: true, name: 'Order Detail', component: DetailOrder },
  { path: '/routes', exact: true, name: 'Routes', component: SolutionRoute },
  { path: '/routes-init', exact: true, name: 'Route Initialization', component: RouteInitialization },
  { path: '/vehicles/detail', exact: true, name: 'Vehicle Detail', component: DetailVehicle },
  { path: '/customers/detail', exact: true, name: 'Customer Detail', component: DetailCustomer },
  { path: '/depots/detail', exact: true, name: 'Depot Detail', component: DetailDepot },
  { path: '/routes/solutions', exact: true, name: 'DeliveryPlans', component: DeliveryPlans },
  { path: '/routes/solutions/tracking', exact: true, name: 'TrackingRoute', component: TrackingRoute },
  { path: '/login', exact: true, name: 'Login', component: Login },
  { path: '/goods-group', exact: true, name: 'Good Groups', component: GoodsGroups },
];

export default routes;
