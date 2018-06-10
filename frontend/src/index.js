import React from 'react';
import ReactDOM from 'react-dom';
import './css/index.css';
import App from './js/App';
import registerServiceWorker from './js/registerServiceWorker';
import 'antd/dist/antd.css'; // or 'antd/dist/antd.less'


ReactDOM.render(<App/>, document.getElementById('root'));
registerServiceWorker();
