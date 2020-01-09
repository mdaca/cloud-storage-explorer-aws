/*
 * Copyright 2010-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.services.dynamodb.datamodeling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDBMapperIntegrationTestBase;

/**
 * Status tests for {@code JsonMarshaller}.
 */
public class JsonMarshallerIntegrationTest extends DynamoDBMapperIntegrationTestBase {

    @Test
    public void testMarshalling() {
        final DynamoDbMapper mapper = new DynamoDbMapper(dynamo);

        final TestObject object1 = new TestObject();
        object1.setOneItem(new TestObject.OneItem());
        object1.addOneItem(new TestObject.OneItem(UUID.randomUUID().toString(), 1));
        object1.addOneItem(new TestObject.OneItem(UUID.randomUUID().toString(), 2));
        object1.addOneItem(new TestObject.OneItem(UUID.randomUUID().toString(), 3));
        object1.setTwoItem(new TestObject.TwoItem());
        object1.addTwoItem(new TestObject.TwoItem(UUID.randomUUID().toString(), new Date()));

        mapper.save(object1);

        assertNotNull(object1.getKey());

        assertNotNull(object1.getOneItem());
        assertNotNull(object1.getOneItems());
        assertEquals(3, object1.getOneItems().size());

        assertNotNull(object1.getTwoItem());
        assertNotNull(object1.getTwoItems());
        assertEquals(1, object1.getTwoItems().size());

        final TestObject object2 = mapper.load(TestObject.class, object1.getKey());

        assertEquals(object1.getKey(), object2.getKey());

        assertEquals(object1.getOneItem().getId(), object2.getOneItem().getId());
        assertEquals(object1.getOneItem().getQuantity(), object2.getOneItem().getQuantity());
        assertEquals(object1.getOneItems().size(), object2.getOneItems().size());

        for (int i = 0, its = object1.getOneItems().size(); i < its; i++) {
            assertEquals(object1.getOneItems().get(i).getId(), object2.getOneItems().get(i).getId());
            assertEquals(object1.getOneItems().get(i).getQuantity(), object2.getOneItems().get(i).getQuantity());
        }

        assertEquals(object1.getTwoItem().getId(), object2.getTwoItem().getId());
        assertEquals(object1.getTwoItem().getDate(), object2.getTwoItem().getDate());
        assertEquals(object1.getTwoItems().size(), object2.getTwoItems().size());

        for (int i = 0, its = object1.getTwoItems().size(); i < its; i++) {
            assertEquals(object1.getTwoItems().get(i).getId(), object2.getTwoItems().get(i).getId());
            assertEquals(object1.getTwoItems().get(i).getDate(), object2.getTwoItems().get(i).getDate());
        }
    }

    @DynamoDbTable(tableName = "aws-java-sdk-util")
    public static class TestObject {
        private String key;


        private OneItem aitem;


        private List<OneItem> oneItems;


        private TwoItem bitem;


        private List<TwoItem> twoItems;

        @DynamoDbHashKey
        @DynamoDbAutoGeneratedKey
        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @DynamoDbMarshalling(marshallerClass = OneItemJsonMarshaller.class)
        public OneItem getOneItem() {
            return this.aitem;
        }

        public void setOneItem(OneItem aitem) {
            this.aitem = aitem;
        }

        @DynamoDbMarshalling(marshallerClass = OneListJsonMarshaller.class)
        public List<OneItem> getOneItems() {
            return this.oneItems;
        }

        public void setOneItems(List<OneItem> oneItems) {
            this.oneItems = oneItems;
        }

        public void addOneItem(OneItem aitem) {
            if (this.oneItems == null) {
                this.oneItems = new ArrayList<OneItem>();
            }
            this.oneItems.add(aitem);
        }

        @DynamoDbMarshalling(marshallerClass = TwoItemJsonMarshaller.class)
        public TwoItem getTwoItem() {
            return this.bitem;
        }

        public void setTwoItem(TwoItem bitem) {
            this.bitem = bitem;
        }

        @DynamoDbMarshalling(marshallerClass = TwoListJsonMarshaller.class)
        public List<TwoItem> getTwoItems() {
            return this.twoItems;
        }

        public void setTwoItems(List<TwoItem> twoItems) {
            this.twoItems = twoItems;
        }

        public void addTwoItem(TwoItem bitem) {
            if (this.twoItems == null) {
                this.twoItems = new ArrayList<TwoItem>();
            }
            this.twoItems.add(bitem);
        }

        public static class OneItemJsonMarshaller extends JsonMarshaller<OneItem> {
        }

        public static class OneListJsonMarshaller extends JsonMarshaller<OneListJsonMarshaller.Type> {
            public OneListJsonMarshaller() {
                super(Type.class);
            }

            ;

            public static final class Type extends ArrayList<OneItem> {
            }
        }

        public static class TwoItemJsonMarshaller extends JsonMarshaller<TwoItem> {
        }

        public static class TwoListJsonMarshaller extends JsonMarshaller<TwoListJsonMarshaller.Type> {
            public TwoListJsonMarshaller() {
                super(Type.class);
            }

            ;

            public static final class Type extends ArrayList<TwoItem> {
            }
        }

        public static class OneItem {
            private String id;
            private Integer quantity;

            public OneItem(String id, Integer quantity) {
                this.id = id;
                this.quantity = quantity;
            }

            public OneItem() {
                this(null, null);
            }

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Integer getQuantity() {
                return this.quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }
        }

        public static class TwoItem {
            private String id;
            private Date date;

            public TwoItem(String id, Date date) {
                this.id = id;
                this.date = date;
            }

            public TwoItem() {
                this(null, null);
            }

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Date getDate() {
                return this.date;
            }

            public void setDate(Date date) {
                this.date = date;
            }
        }
    }
}
