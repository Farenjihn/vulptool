import React from "react";

import { Layout} from 'antd';
const {Content} = Layout;


class Welcome extends React.Component {

    render() {
        return (
            <Content style={{ margin: '16px 16px' }}>
                <h1>
                    Welcome to Vulptool !
                </h1>
            </Content>
        );
    }
}

export default Welcome;
